package com.ecommerce.gateway_service.service;

import com.ecommerce.gateway_service.dto.JwtUserDto;

public interface JwtValidatorService {
    boolean validateToken(String token);
    JwtUserDto extractUserDto(String token);
}
