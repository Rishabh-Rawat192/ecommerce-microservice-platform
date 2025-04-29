package com.ecommerce.user_service.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserProfileUpdateRequest(
        String fullName,
        @Size(min =10, max = 10, message = "Phone number must be at most 10 digits")
        @Pattern(regexp = "^[0-9]*$", message = "Invalid phone number")
        String phoneNumber,
        String address
) {
}
