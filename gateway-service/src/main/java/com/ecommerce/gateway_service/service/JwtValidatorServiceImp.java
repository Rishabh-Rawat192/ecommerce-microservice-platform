package com.ecommerce.gateway_service.service;

import com.ecommerce.gateway_service.dto.JwtUserDto;
import com.ecommerce.gateway_service.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class JwtValidatorServiceImp implements JwtValidatorService{

    private final String secret;
    private final Logger logger = LogManager.getLogger(JwtValidatorServiceImp.class);

    public JwtValidatorServiceImp(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    @Override
    public JwtUserDto extractUserDto(String token) {
        Claims claims = getPayload(token);
        UUID uuid = UUID.fromString(claims.getSubject());
        String email = claims.get("email", String.class);
        Role role = Role.valueOf(claims.get("role", String.class));

        return new JwtUserDto(uuid, email, role);
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private Claims getPayload(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
