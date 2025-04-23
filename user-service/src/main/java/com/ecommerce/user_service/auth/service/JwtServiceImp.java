package com.ecommerce.user_service.auth.service;

import com.ecommerce.user_service.auth.dto.JwtUserDto;
import com.ecommerce.user_service.common.enums.Role;
import com.ecommerce.user_service.common.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtServiceImp implements JwtService{
    private final String secret;
    private final long expirationMs;

    public JwtServiceImp(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expirationMs}") long expirationMs) {
        this.secret = secret;
        this.expirationMs = expirationMs;
    }

    @Override
    public String generateToken(JwtUserDto userDto) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userDto.email());
        claims.put("role", userDto.role());

        return Jwts.builder()
                .claims(claims)
                .subject(userDto.userId().toString())
                .signWith(getSignInKey())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + this.expirationMs))
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        } catch (JwtException e) {
            throw new ApiException("Token validation failed.", HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public JwtUserDto extractUserDto(String token) {
        try {
            Claims claims= Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            UUID userId = UUID.fromString(claims.getSubject());
            String email = claims.get("email", String.class);
            Role role = Role.valueOf(claims.get("role", String.class));

            return new JwtUserDto(userId, email, role);
        } catch (Exception e) {
            throw new ApiException("Token parsing failed.", HttpStatus.UNAUTHORIZED);
        }
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
