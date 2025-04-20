package com.ecommerce.user_service.auth.dto;

public record RegisterRequest(
        String name,
        String email,
        String password
) {}
