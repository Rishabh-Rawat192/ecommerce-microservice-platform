package com.ecommerce.user_service.user.service;

import com.ecommerce.user_service.user.dto.UserProfileRegisterRequest;
import com.ecommerce.user_service.user.dto.UserProfileRegisterResponse;

import java.util.UUID;

public interface UserProfileService {
    UserProfileRegisterResponse register(UserProfileRegisterRequest request, UUID userId);
}
