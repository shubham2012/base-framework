package com.base.service.config.interceptor;

import com.base.commons.enums.APIVersion;
import com.base.commons.enums.Locale;
import com.base.commons.enums.RequestSource;
import com.base.commons.enums.Role;
import com.base.commons.exception.UnauthorizedException;
import com.base.commons.utils.Context;
import com.base.commons.utils.ContextInfo;
import com.base.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Request interceptor is taking care of all the necessary steps that are supposed to be taken before request is being
 * handing over to the controller Its taking care of Logging, Auth etc.
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static String SWAGGER = "swagger";
    private static String HEARTBEAT = "heartbeat";

    @Value("${auth.exclusions}")
    private String[] AUTH_EXCLUSIONS;

    private final JwtUtil authProvider;

    private static final String VERSION_HEADER_KEY = "Accept-Version";

    private static final String LANGUAGE_HEADER_KEY = "accept-language";

    public static final String REQUEST_SOURCE_HEADER_KEY = "source";

    public static final String APP_VERSION_HEADER_KEY = "app_version";

    /**
     * PreHandle for any request
     *
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestURI = request.getRequestURI();
        String requestLogDetails = "Requested URI: " + requestURI;
        String sourceValue = request.getHeader(REQUEST_SOURCE_HEADER_KEY);
        if (!StringUtils.isEmpty(sourceValue)) {
            requestLogDetails += " Request Source: " + sourceValue;
        }
        log.info(requestLogDetails);

        if (requestURI.contains(SWAGGER) || requestURI.contains(HEARTBEAT)) {
            return true;
        }

        if (Objects.nonNull(AUTH_EXCLUSIONS) && AUTH_EXCLUSIONS.length > 0) {
            for (String ex : AUTH_EXCLUSIONS) {
                if (requestURI.contains(ex)) {
                    return true;
                }
            }
        }

        authHandler(request);
        // Read version from request and set to Context
        APIVersion version =
                APIVersion.getFromStringOrDefault(request.getHeader(VERSION_HEADER_KEY), APIVersion.V1_0_0);
        Context.getContextInfo().setApiVersion(version);
        RequestSource requestSource =
                RequestSource.getOrDefault(request.getHeader(REQUEST_SOURCE_HEADER_KEY), RequestSource.ADMIN_PANEL);
        Context.getContextInfo().setRequestSource(requestSource);
        final String appVersion = request.getHeader(APP_VERSION_HEADER_KEY);
        if (StringUtils.isNotBlank(appVersion)) {
            Context.getContextInfo().setAppVersion(appVersion);
        }
        return true;
    }

    /**
     * Validate Authentication and Authorization Throws UnauthorizedException in case of Auth fails
     *
     * @param request
     */
    private void authHandler(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.isBlank(token)) {
            throw new UnauthorizedException("Authentication token not found");
        }
        String locale = request.getHeader("accept-language");
        if (StringUtils.isNotBlank(locale) && Locale.EN.get().equals(locale)) {
            Context.getContextInfo().setLocale(Locale.EN);
        } else {
            Context.getContextInfo().setLocale(Locale.EN);
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Jwt<Header, Claims> claims = authProvider.parseToken(token);
        log.info(claims.getBody().toString());
        String roleIdentifier = claims.getBody().get("custom:role").toString();
        Role role = Role.get(roleIdentifier);
        String id = claims.getBody().get("custom:id") == null ? null : claims.getBody().get("custom:id").toString();
        String eventId = claims.getBody().get("event_id") == null ? null : claims.getBody().get("event_id").toString();
        String sub = claims.getBody().get("sub") == null ? null : claims.getBody().get("sub").toString();
        String service = claims.getBody().get("service") == null ? null : claims.getBody().get("service").toString();

        if ((!Role.SERVICE.equals(role) && StringUtils.isEmpty(id))) {
            throw new UnauthorizedException("Bad auth token");
        }

        Context.getContextInfo().setRole(roleIdentifier);
        Context.getContextInfo().setSub(sub);
        Context.getContextInfo().setEventId(eventId);
        Context.getContextInfo().setId(id);
        Context.getContextInfo().setService(service);
        Context.getContextInfo().setRemoteAddress(request.getRemoteAddr());
    }

    /**
     * Operations to be performed after a request is completed, to prepare the thread for new incoming request
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        Context.setContextInfo(new ContextInfo());
    }
}
