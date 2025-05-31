package com.ecommerce.inventory_service.dto;

import java.util.UUID;

public record UserDto(
        UUID userId,
        String email,
        Role role
) {
}
