package com.ecommerce.user_service.auth.controller;

import com.ecommerce.user_service.auth.dto.AuthRequest;
import com.ecommerce.user_service.auth.dto.LoginResponse;
import com.ecommerce.user_service.auth.dto.RegisterResponse;
import com.ecommerce.user_service.auth.service.AuthService;
import com.ecommerce.user_service.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", authService.register(request)));
    }

    @PostMapping("/login")
    ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(ApiResponse.success("User logged in successfully", authService.login(request)));
    }
}
