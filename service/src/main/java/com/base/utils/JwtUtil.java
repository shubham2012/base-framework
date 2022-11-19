package com.base.utils;

import com.base.commons.enums.Role;
import com.base.commons.exception.UnauthorizedException;
import com.base.commons.utils.Context;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.jsonwebtoken.impl.crypto.JwtSignatureValidator;
import java.security.Key;
import java.util.Date;
import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** JWT util takes care all of our JWT related work This util is being used to parse the JWT token and to create one */
@Slf4j
@Component
public class JwtUtil {

    private final String secretKey;

    protected final Long validity;

    private final String serviceName;

    private final byte[] secretKeyBytes;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.validity}") Long validity,
            @Value("${service.name}") String serviceName) {
        this.secretKey = secretKey;
        this.validity = validity;
        this.serviceName = serviceName;
        this.secretKeyBytes = secretKey.getBytes();
    }

    /**
     * Create JWT Token by only passing the role
     *
     * @param role
     * @return
     */
    public String createToken(Role role) {
        String id = Context.getContextInfo().getId();
        if (StringUtils.isEmpty(id)) {
            id = serviceName;
        }
        return createToken(id, role);
    }

    /**
     * Creates a signingKey for creating and validating tokens based on the key from properties
     *
     * @return
     */
    public Key getSigningKey(SignatureAlgorithm algorithm) {
        return new SecretKeySpec(secretKeyBytes, algorithm.getJcaName());
    }

    /**
     * Create JWT Token by only passing the role & user context
     *
     * @param id
     * @param role
     * @return
     */
    public String createToken(String id, Role role) {

        Claims claims = Jwts.claims().setSubject(id);
        claims.put("custom:role", role.get());
        claims.put("custom:id", id);
        if (role.equals(Role.SERVICE)) {
            claims.put("service", serviceName);
        }
        Date now = new Date();
        Date validity = new Date(now.getTime() + this.validity);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, getSigningKey(SignatureAlgorithm.HS256))
                .compact();
    }

    /**
     * Get Sub from the token
     *
     * @param token
     * @return
     */
    public String getSub(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * @param userId
     * @param phoneNumber
     * @param role
     * @param validityTime
     * @return
     */
    public String createAuthTokenForThirdParty(String userId, String phoneNumber, Role role, Long validityTime) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("custom:role", role.get());
        claims.put("custom:id", userId);
        if (StringUtils.isNotEmpty(phoneNumber)) {
            claims.put("custom:phoneNumber", phoneNumber);
        }
        claims.put("type", role.THIRD_PARTY);
        if (Objects.nonNull(role)) {
            claims.put("service", role.toString());
        }
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, getSigningKey(SignatureAlgorithm.HS256))
                .compact();
    }

    /**
     * parseToken the JWT token
     *
     * @param token
     * @return
     */
    public Jwt<Header, Claims> parseToken(String token) {
        try {
            String body = token.substring(0, token.lastIndexOf('.') + 1);
            Jwt<Header, Claims> claims = Jwts.parser().parseClaimsJwt(body);
            if (Role.SERVICE.equals(Role.get(claims.getBody().get("custom:role").toString()))) {
                log.info("Checking token validity for service calls");
                validateTokenForService(claims.getHeader().get("alg").toString(), token);
            }
            return claims;
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException("Expired or invalid JWT token");
        }
    }

    public void validateTokenForService(String alg, String token) throws SignatureException {
        SignatureAlgorithm algorithm = SignatureAlgorithm.forName(alg);
        Key key = getSigningKey(algorithm);
        JwtSignatureValidator validator = new DefaultJwtSignatureValidator(algorithm, key);
        String[] splitToken = token.split("[.]");
        if (splitToken.length != 3 || !validator.isValid(splitToken[0] + "." + splitToken[1], splitToken[2])) {
            throw new UnauthorizedException("Signature does not match. Token cannot be trusted");
        }
        log.info("Successfully validated the JWT token");
    }
}
