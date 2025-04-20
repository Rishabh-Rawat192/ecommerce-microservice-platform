package com.ecommerce.user_service.auth.dto;

public record LoginRequest(
        String email,
        String password) {}
