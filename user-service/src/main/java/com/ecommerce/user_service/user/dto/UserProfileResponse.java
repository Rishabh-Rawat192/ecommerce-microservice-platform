package com.ecommerce.user_service.user.dto;

import java.util.UUID;

public record UserProfileResponse(
        UUID userId,
        String fullName,
        String phoneNumber,
        String address
) {}
