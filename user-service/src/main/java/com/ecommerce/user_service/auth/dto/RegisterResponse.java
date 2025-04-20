package com.ecommerce.user_service.auth.dto;


import java.util.UUID;

public record RegisterResponse(
        UUID userId,
        String email,
        String name) {
}
