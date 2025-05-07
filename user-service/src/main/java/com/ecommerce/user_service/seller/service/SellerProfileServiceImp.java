package com.ecommerce.user_service.seller.service;

import com.ecommerce.user_service.auth.UserRepository;
import com.ecommerce.user_service.auth.entity.User;
import com.ecommerce.user_service.common.enums.Role;
import com.ecommerce.user_service.common.exception.ApiException;
import com.ecommerce.user_service.seller.dto.SellerProfileRegisterRequest;
import com.ecommerce.user_service.seller.dto.SellerProfileRegisterResponse;
import com.ecommerce.user_service.seller.dto.SellerProfileResponse;
import com.ecommerce.user_service.seller.dto.SellerProfileUpdateRequest;
import com.ecommerce.user_service.seller.entity.SellerProfile;
import com.ecommerce.user_service.seller.repository.SellerProfileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerProfileServiceImp implements SellerProfileService {
    private final SellerProfileRepository sellerProfileRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SellerProfileRegisterResponse register(SellerProfileRegisterRequest request, UUID userId) {
        if (sellerProfileRepository.existsById(userId))
            throw new ApiException("Seller Profile already exists.", HttpStatus.CONFLICT);

        User user = userRepository.findById(userId)
                .orElseThrow( () ->new ApiException("User not found.", HttpStatus.UNAUTHORIZED));

        user.setRole(Role.SELLER);

        SellerProfile sellerProfile = SellerProfile.builder()
                .user(user)
                .businessName(request.businessName().trim())
                .gstNumber(request.gstNumber().trim())
                .phoneNumber(request.phoneNumber())
                .address(request.address().trim())
                .isEnabled(true)
                .build();

        sellerProfileRepository.save(sellerProfile);
        userRepository.save(user);

        return new SellerProfileRegisterResponse(sellerProfile.getId());
    }

    @Override
    public SellerProfileResponse get(UUID userId) {
        SellerProfile sellerProfile = sellerProfileRepository.findById(userId)
                .orElseThrow(()-> new ApiException("Seller Profile not found.", HttpStatus.NOT_FOUND));

        return new SellerProfileResponse(sellerProfile.getId(),
                sellerProfile.getBusinessName(), sellerProfile.getGstNumber(),
                sellerProfile.getAddress(), sellerProfile.getPhoneNumber());
    }

    @Override
    public SellerProfileResponse update(SellerProfileUpdateRequest request, UUID userId) {
        SellerProfile sellerProfile = sellerProfileRepository.findById(userId)
                .orElseThrow(()-> new ApiException("Seller Profile not found.", HttpStatus.NOT_FOUND));

        if (request.businessName() != null && !request.businessName().isBlank())
            sellerProfile.setBusinessName(request.businessName().trim());
        if (request.gstNumber() != null && !request.gstNumber().isBlank())
            sellerProfile.setGstNumber(request.gstNumber().trim());
        if (request.address() != null && !request.address().isBlank())
            sellerProfile.setAddress(request.address().trim());
        if (request.phoneNumber() != null && !request.phoneNumber().isBlank())
            sellerProfile.setPhoneNumber(request.phoneNumber());

        sellerProfileRepository.save(sellerProfile);

        return new SellerProfileResponse(sellerProfile.getId(),
                sellerProfile.getBusinessName(), sellerProfile.getGstNumber(),
                sellerProfile.getAddress(), sellerProfile.getPhoneNumber());
    }
}
