package com.ecommerce.user_service.auth.service;

import com.ecommerce.user_service.auth.dto.JwtUserDto;
import com.ecommerce.user_service.common.enums.Role;
import com.ecommerce.user_service.common.exception.ApiException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceImpTest {
    private JwtServiceImp jwtService;
    private final String secret = "my-very-secure-and-long-jwt-secret-key-used-for-tests";
    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        // 1 hour
        long expirationMs = 3600000;
        jwtService = new JwtServiceImp(secret, expirationMs);
    }

    @Test
    void validateToken_shouldThrowExceptionForExpiredToken() {
        // Arrange
        long expiredExpirationMs = -1000; // Token already expired
        JwtServiceImp expiredJwtService = new JwtServiceImp(secret, expiredExpirationMs);

        JwtUserDto userDto = new JwtUserDto(UUID.randomUUID(), "expired@example.com", Role.USER);
        String expiredToken = expiredJwtService.generateToken(userDto);

        // Act & Assert
        ApiException ex = assertThrows(ApiException.class, () -> jwtService.validateToken(expiredToken));
        assertEquals("Token validation failed.", ex.getMessage());
    }

    @Test
    void extractUserDto_shouldThrowExceptionForTokenWithMissingClaims() throws Exception {
        // Arrange
        String tokenWithMissingClaims = Jwts.builder()
                .subject(UUID.randomUUID().toString())
                .signWith(invokeGetSignInKey())
                .compact();

        // Act & Assert
        ApiException ex = assertThrows(ApiException.class, () -> jwtService.extractUserDto(tokenWithMissingClaims));
        assertEquals("Token parsing failed.", ex.getMessage());
    }

    @Test
    void validateToken_shouldThrowExceptionForTokenWithInvalidSignature() {
        // Arrange
        String invalidSignatureToken = Jwts.builder()
                .subject(UUID.randomUUID().toString())
                .signWith(Keys.hmacShaKeyFor("difffffffffffffffffffferent-secret-key".getBytes(StandardCharsets.UTF_8)))
                .compact();

        // Act & Assert
        ApiException ex = assertThrows(ApiException.class, () -> jwtService.validateToken(invalidSignatureToken));
        assertEquals("Token validation failed.", ex.getMessage());
    }

    @Test
    void generateToken_shouldReturnValidJwt() {
        JwtUserDto jwtUserDto = new JwtUserDto(UUID.randomUUID(), "test@gmail.com", Role.USER);

        String token = jwtService.generateToken(jwtUserDto);

        assertNotNull(token);
        assertTrue(jwtService.validateToken(token));

        JwtUserDto extractUserDto = jwtService.extractUserDto(token);
        assertEquals(jwtUserDto.email(), extractUserDto.email());
        assertEquals(jwtUserDto.userId(), extractUserDto.userId());
        assertEquals(jwtUserDto.role(), extractUserDto.role());
    }

    @Test
    void validateToken_shouldReturnTrueForValidToken() {
        JwtUserDto userDto = new JwtUserDto(UUID.randomUUID(), "test@example.com", Role.SELLER);
        String token = jwtService.generateToken(userDto);

        assertTrue(jwtService.validateToken(token));
    }

    @Test
    void validateToken_shouldThrowExceptionForInvalidToken() {
        String invalidToken = "invalid.token.string";

        ApiException ex = assertThrows(ApiException.class, () -> jwtService.validateToken(invalidToken));
        assertEquals("Token validation failed.", ex.getMessage());
    }

    @Test
    void extractUserDto_shouldThrowExceptionForInvalidToken() {
        String invalidToken = "invalid.token.string";

        ApiException ex = assertThrows(ApiException.class, () -> jwtService.extractUserDto(invalidToken));
        assertEquals("Token parsing failed.", ex.getMessage());
    }

    // Helper method to invoke the private getSignInKey method
    private SecretKey invokeGetSignInKey() throws Exception {
        java.lang.reflect.Method method = JwtServiceImp.class.getDeclaredMethod("getSignInKey");
        method.setAccessible(true);
        return (SecretKey) method.invoke(jwtService);
    }
}
