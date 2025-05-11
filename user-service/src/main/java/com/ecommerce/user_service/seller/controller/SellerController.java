package com.ecommerce.user_service.seller.controller;

import com.ecommerce.user_service.auth.dto.JwtUserDto;
import com.ecommerce.user_service.common.dto.ApiResponse;
import com.ecommerce.user_service.common.exception.ApiException;
import com.ecommerce.user_service.seller.dto.SellerProfileRegisterRequest;
import com.ecommerce.user_service.seller.dto.SellerProfileRegisterResponse;
import com.ecommerce.user_service.seller.dto.SellerProfileResponse;
import com.ecommerce.user_service.seller.dto.SellerProfileUpdateRequest;
import com.ecommerce.user_service.seller.entity.SellerProfile;
import com.ecommerce.user_service.seller.service.KafkaProducerService;
import com.ecommerce.user_service.seller.service.SellerProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/sellers")
@RequiredArgsConstructor
public class SellerController {
    private final SellerProfileService sellerProfileService;
    private final KafkaProducerService kafkaProducerService;

    @PostMapping
    public ResponseEntity<ApiResponse<SellerProfileRegisterResponse>>
    registerProfile(@Valid @RequestBody SellerProfileRegisterRequest request,
                    @AuthenticationPrincipal JwtUserDto userDto) {
        SellerProfileRegisterResponse response = sellerProfileService.register(request, userDto.userId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Seller Profile Registered Successfully.",
                        response));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<SellerProfileResponse>>
    getProfile(@PathVariable UUID userId, @AuthenticationPrincipal JwtUserDto userDto) {
        if (!userId.equals(userDto.userId()))
            throw new ApiException("Unauthorize Access.", HttpStatus.UNAUTHORIZED);

        SellerProfileResponse response = sellerProfileService.get(userId);
        return ResponseEntity.ok(ApiResponse.success("Seller Profile Found Successfully.", response));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<ApiResponse<SellerProfileResponse>>
    updateProfile(@Valid @RequestBody SellerProfileUpdateRequest request,
                  @PathVariable UUID userId, JwtUserDto userDto) {
        if (!userId.equals(userDto.userId()))
            throw new ApiException("Unauthorize Access.", HttpStatus.UNAUTHORIZED);

        SellerProfileResponse response = sellerProfileService.update(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Seller Profile Updated Successfully.",
                        response));
    }

    @GetMapping("/test/kafka")
    public ResponseEntity<ApiResponse<String>> kafkaTestSend() {
        // Example UUID and status
        UUID sellerId = UUID.randomUUID();
        boolean isActive = true;
        // Send the message to Kafka
        kafkaProducerService.sendSellerStatusUpdate(sellerId, isActive);

        return ResponseEntity.ok(ApiResponse.success("Testing kafka send", null));
    }
}
