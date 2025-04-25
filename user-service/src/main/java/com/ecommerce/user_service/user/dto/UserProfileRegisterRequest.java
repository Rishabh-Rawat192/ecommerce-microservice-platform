package com.ecommerce.user_service.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserProfileRegisterRequest(
        @NotBlank
        String fullName,
        @NotBlank
        @Size(min = 10, max = 10)
        @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number")
        String phoneNumber,
        @NotBlank
        String address
) { }
