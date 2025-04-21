package com.ecommerce.user_service.auth.service;

import com.ecommerce.user_service.auth.UserRepository;
import com.ecommerce.user_service.auth.dto.AuthRequest;
import com.ecommerce.user_service.auth.dto.JwtUserDto;
import com.ecommerce.user_service.auth.dto.LoginResponse;
import com.ecommerce.user_service.auth.dto.RegisterResponse;
import com.ecommerce.user_service.auth.entity.User;
import com.ecommerce.user_service.common.enums.Role;
import com.ecommerce.user_service.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService{
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImp.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public RegisterResponse register(AuthRequest request) {
        logger.info("Registering user with email {}", request.email());
        if (userRepository.existsByEmail(request.email()))
            throw new ApiException("User already exists", HttpStatus.BAD_REQUEST);

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .isEnabled(true)
                .build();

        userRepository.save(user);
        logger.info("User registered successfully with email: {}", user.getEmail());
        return new RegisterResponse(user.getId(), user.getEmail(), user.getRole());
    }

    @Override
    public LoginResponse login(AuthRequest request) {
        logger.info("Attempting login for email: {}", request.email());
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ApiException("Invalid credentials", HttpStatus.UNAUTHORIZED));

        if (!passwordEncoder.matches(request.password(), user.getPassword()))
            throw new ApiException("Invalid credentials", HttpStatus.UNAUTHORIZED);

        String token = jwtService.generateToken(new JwtUserDto(user.getId(), user.getEmail(), user.getRole()));

        logger.info("User logged in successfully with email: {}", user.getEmail());
        return new LoginResponse(user.getId(), user.getEmail(), token);
    }
}
