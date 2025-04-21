package com.ecommerce.user_service.auth.dto;

import com.ecommerce.user_service.common.enums.Role;

import java.util.UUID;

public record JwtUserDto(UUID userId, String email, Role role) {
}
