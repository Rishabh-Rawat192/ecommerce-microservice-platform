package com.ecommerce.user_service.seller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SellerProfileUpdateRequest(
        String businessName,
        @Size(min = 20, max = 20)
        String gstNumber,
        String address,
        @Size(min = 10, max = 10)
        @Pattern(regexp = "^[0-9]{10}$", message = "Invalid phone number")
        String phoneNumber
) { }
