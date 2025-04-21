package com.ecommerce.user_service.auth.service;

import com.ecommerce.user_service.auth.dto.AuthRequest;
import com.ecommerce.user_service.auth.dto.LoginResponse;
import com.ecommerce.user_service.auth.dto.RegisterResponse;

public interface AuthService {
    RegisterResponse register(AuthRequest request);
    LoginResponse login(AuthRequest request);
}
