package com.ecommerce.user_service.user.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UserProfileRegisterRequest(
        @NotBlank
        String fullName,
        @NotBlank
        String phoneNumber,
        @NotBlank
        String address
) { }
