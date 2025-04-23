package com.ecommerce.user_service.auth.service;

import com.ecommerce.user_service.auth.UserRepository;
import com.ecommerce.user_service.auth.dto.AuthRequest;
import com.ecommerce.user_service.auth.dto.JwtUserDto;
import com.ecommerce.user_service.auth.dto.LoginResponse;
import com.ecommerce.user_service.auth.dto.RegisterResponse;
import com.ecommerce.user_service.auth.entity.User;
import com.ecommerce.user_service.common.enums.Role;
import com.ecommerce.user_service.common.exception.ApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImp authService;

    private User dummyUser;
    private String encodedPassword;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        encodedPassword = "encoded_password";
        jwtToken = "jwt.token.here";

        dummyUser = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .password(encodedPassword)
                .role(Role.USER)
                .build();
    }

    @Test
    void login_shouldReturnJwtToken_onValidCredentials() {
        AuthRequest request = new AuthRequest("test@example.com", "plain_password");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(dummyUser));
        when(passwordEncoder.matches(request.password(), dummyUser.getPassword())).thenReturn(true);
        when(jwtService.generateToken(any(JwtUserDto.class))).thenReturn(jwtToken);

        LoginResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals(jwtToken, response.token());

        verify(userRepository).findByEmail(request.email());
        verify(passwordEncoder).matches(request.password(), dummyUser.getPassword());
        verify(jwtService).generateToken(any());
    }

    @Test
    void login_shouldThrowException_onUserNotFound() {
        AuthRequest request = new AuthRequest("notfound@example.com", "password");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        ApiException ex = assertThrows(ApiException.class, () -> authService.login(request));
        assertEquals("Invalid credentials", ex.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getHttpStatus());
    }

    @Test
    void login_shouldThrowException_onInvalidPassword() {
        AuthRequest request = new AuthRequest("test@example.com", "wrong_password");

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(dummyUser));
        when(passwordEncoder.matches(request.password(), dummyUser.getPassword())).thenReturn(false);

        ApiException ex = assertThrows(ApiException.class, () -> authService.login(request));
        assertEquals("Invalid credentials", ex.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getHttpStatus());
    }

    @Test
    void login_shouldThrowException_onNullEmail() {
        AuthRequest request = new AuthRequest(null, "password");

        ApiException ex = assertThrows(ApiException.class, () -> authService.login(request));
        assertEquals("Invalid credentials", ex.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getHttpStatus());
    }

    @Test
    void login_shouldThrowException_onEmptyEmail() {
        AuthRequest request = new AuthRequest("", "password");

        ApiException ex = assertThrows(ApiException.class, () -> authService.login(request));
        assertEquals("Invalid credentials", ex.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getHttpStatus());
    }

    @Test
    void login_shouldThrowException_onNullPassword() {
        AuthRequest request = new AuthRequest("test@example.com", null);

        ApiException ex = assertThrows(ApiException.class, () -> authService.login(request));
        assertEquals("Invalid credentials", ex.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getHttpStatus());
    }

    @Test
    void login_shouldThrowException_onEmptyPassword() {
        AuthRequest request = new AuthRequest("test@example.com", "");

        ApiException ex = assertThrows(ApiException.class, () -> authService.login(request));
        assertEquals("Invalid credentials", ex.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, ex.getHttpStatus());
    }


    @Test
    void register_shouldCreateUser_onValidRequest() {
        AuthRequest request = new AuthRequest(dummyUser.getEmail(), "plain_password");

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(request.password())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(dummyUser);

        RegisterResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals(dummyUser.getEmail(), response.email());

        verify(userRepository).existsByEmail(request.email());
        verify(passwordEncoder).encode(request.password());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrowException_onDuplicateEmail() {
        AuthRequest request = new AuthRequest("test@example.com", "plain_password");

        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        ApiException ex = assertThrows(ApiException.class, () -> authService.register(request));
        assertEquals("User already exists", ex.getMessage());
        assertEquals(HttpStatus.CONFLICT, ex.getHttpStatus());

        verify(userRepository).existsByEmail(request.email());
    }
}
