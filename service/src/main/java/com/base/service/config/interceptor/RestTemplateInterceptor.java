package com.base.service.config.interceptor;

import static com.base.commons.utils.BaseConstants.REQUEST_SOURCE;

import com.base.commons.enums.Role;
import com.base.utils.JwtUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplateInterceptor intercepts the any outgoing call using Rest template and handles the request and also adds
 * any additional parameters to request which are needed. It adds authentication and also takes care of retry
 */
@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Value("${external.call.ttl}")
    private Integer ttl;

    @Value("${external.call.retry.count}")
    private Integer retryCount;

    @Value("${service.name}")
    private String serviceName;

    @Value("${external.call.timeout:10000}")
    private Integer timeout;

    private final JwtUtil jwtUtil;

    /**
     * Intercept the outgoing request and add additional parameters to it
     *
     * @param request
     * @param body
     * @param execution
     * @return
     * @throws IOException
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        request.getHeaders().setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        request.getHeaders().setBearerAuth(jwtUtil.createToken(Role.SERVICE));
        request.getHeaders().set(REQUEST_SOURCE, serviceName);
        log.info("Out going call to : " + request.getURI());
        log.info("Data: " + new String(body, StandardCharsets.UTF_8));
        ClientHttpResponse response = execution.execute(request, body);
        return response;
    }

    /**
     * Rest template Bean
     *
     * @param clientFactory
     * @return
     */
    @Bean
    public RestTemplate customRestTemplate(@Qualifier("clientFactory") ClientHttpRequestFactory clientFactory) {
        RestTemplate restTemplate = new RestTemplate(clientFactory);
        restTemplate.setInterceptors(Collections.singletonList(this::intercept));
        return restTemplate;
    }

    /**
     * Handling retry for the rest template
     *
     * @return
     */
    @Bean
    public ClientHttpRequestFactory clientFactory() {
        HttpClient httpClient = HttpClients.custom().setConnectionTimeToLive(ttl, TimeUnit.MINUTES).build();
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        clientHttpRequestFactory.setConnectTimeout(timeout);
        clientHttpRequestFactory.setReadTimeout(timeout);
        return clientHttpRequestFactory;
    }
}
