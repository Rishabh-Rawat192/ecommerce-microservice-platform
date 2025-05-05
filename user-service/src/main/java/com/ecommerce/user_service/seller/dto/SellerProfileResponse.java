package com.ecommerce.user_service.seller.dto;

import java.util.UUID;

public record SellerProfileResponse(
        UUID userId,
        String businessName,
        String gstNumber,
        String address,
        String phoneNumber
) { }
