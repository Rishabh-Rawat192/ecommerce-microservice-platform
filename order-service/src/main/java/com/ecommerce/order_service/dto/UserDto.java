package com.ecommerce.order_service.dto;

import com.ecommerce.order_service.enums.Role;

import java.util.UUID;

public record UserDto(
        UUID userId,
        String email,
        Role role
) { }
