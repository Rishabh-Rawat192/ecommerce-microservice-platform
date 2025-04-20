package com.ecommerce.user_service.auth.controller;

import com.ecommerce.user_service.auth.dto.LoginRequest;
import com.ecommerce.user_service.auth.dto.LoginResponse;
import com.ecommerce.user_service.auth.dto.RegisterRequest;
import com.ecommerce.user_service.auth.dto.RegisterResponse;
import com.ecommerce.user_service.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/register")
    ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody RegisterRequest registerRequest) {
        System.out.println(registerRequest);
        return ResponseEntity.ok(ApiResponse.success("registered",
                new RegisterResponse(UUID.randomUUID(),registerRequest.email(),registerRequest.name())));
    }

    @PostMapping("/login")
    ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest);
        return ResponseEntity.ok(ApiResponse.success("registered",
                new LoginResponse(UUID.randomUUID(), loginRequest.email(), "token")));
    }
}
