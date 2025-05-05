package com.ecommerce.user_service.seller.service;

import com.ecommerce.user_service.seller.dto.SellerProfileRegisterRequest;
import com.ecommerce.user_service.seller.dto.SellerProfileRegisterResponse;
import com.ecommerce.user_service.seller.dto.SellerProfileResponse;
import com.ecommerce.user_service.seller.dto.SellerProfileUpdateRequest;
import com.ecommerce.user_service.seller.entity.SellerProfile;

import java.util.UUID;

public interface SellerProfileService {
    SellerProfileRegisterResponse register(SellerProfileRegisterRequest request,
                                           UUID userId);
    SellerProfileResponse get(UUID userId);
    SellerProfileResponse update(SellerProfileUpdateRequest request,
                                 UUID userId);
}
