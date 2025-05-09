package com.ecommerce.gateway_service.service;

import com.ecommerce.gateway_service.dto.JwtUserDto;

public interface JwtValidatorService {
    JwtUserDto extractUserDto(String token);
}
