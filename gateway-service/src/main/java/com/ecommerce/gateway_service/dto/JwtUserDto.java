package com.ecommerce.gateway_service.dto;


import com.ecommerce.gateway_service.enums.Role;

import java.util.UUID;

public record JwtUserDto(UUID userId, String email, Role role) {
}
