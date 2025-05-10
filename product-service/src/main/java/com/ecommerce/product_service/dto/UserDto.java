package com.ecommerce.product_service.dto;

import com.ecommerce.product_service.enums.Role;

import java.util.UUID;

public record UserDto(UUID userId, String email, Role role) {
}
