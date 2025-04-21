package com.ecommerce.user_service.auth.service;

import com.ecommerce.user_service.auth.dto.JwtUserDto;

public interface JwtService {
    String generateToken(JwtUserDto userDto);
    boolean validateToken(String token);
    JwtUserDto extractUserDto(String token);
}
