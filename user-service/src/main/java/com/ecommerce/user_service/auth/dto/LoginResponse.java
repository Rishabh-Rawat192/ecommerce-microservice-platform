package com.ecommerce.user_service.auth.dto;

import java.util.UUID;

public record LoginResponse(
        UUID uuid,
        String email,
        String token) {}
