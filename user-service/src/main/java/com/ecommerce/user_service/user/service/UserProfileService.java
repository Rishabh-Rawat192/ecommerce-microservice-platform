package com.ecommerce.user_service.user.service;

import com.ecommerce.user_service.user.dto.UserProfileRegisterRequest;
import com.ecommerce.user_service.user.dto.UserProfileRegisterResponse;
import com.ecommerce.user_service.user.dto.UserProfileResponse;
import com.ecommerce.user_service.user.dto.UserProfileUpdateRequest;

import java.util.UUID;

public interface UserProfileService {
    UserProfileRegisterResponse register(UserProfileRegisterRequest request, UUID userId);
    UserProfileResponse get(UUID userId);
    UserProfileResponse update(UserProfileUpdateRequest request, UUID userId);
}
