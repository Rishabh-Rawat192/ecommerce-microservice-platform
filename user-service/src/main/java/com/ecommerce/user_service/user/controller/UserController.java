package com.ecommerce.user_service.user.controller;

import com.ecommerce.user_service.auth.dto.JwtUserDto;
import com.ecommerce.user_service.common.dto.ApiResponse;
import com.ecommerce.user_service.user.dto.UserProfileRegisterRequest;
import com.ecommerce.user_service.user.dto.UserProfileRegisterResponse;
import com.ecommerce.user_service.user.dto.UserProfileResponse;
import com.ecommerce.user_service.user.service.UserProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);
    private final UserProfileService userProfileService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserProfileRegisterResponse>> registerProfile(@Valid @RequestBody UserProfileRegisterRequest userProfileRequest, @AuthenticationPrincipal JwtUserDto jwtUserDto) {
        UUID userId = jwtUserDto.userId();
        logger.info("userId is:{}", userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse
                        .success("User profile registered successfully",
                                 userProfileService.register(userProfileRequest, userId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<UserProfileResponse>> getCurrentUserProfile(HttpServletRequest request, @AuthenticationPrincipal JwtUserDto jwtUserDto) {
        UUID userId = jwtUserDto.userId();
        logger.info("userId is:{}", userId);
        return ResponseEntity.ok(
                ApiResponse.success("User profile found successfully.",
                        userProfileService.get(userId)));
    }
}
