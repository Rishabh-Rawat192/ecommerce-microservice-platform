package com.ecommerce.user_service.user.service;

import com.ecommerce.user_service.auth.UserRepository;
import com.ecommerce.user_service.auth.entity.User;
import com.ecommerce.user_service.common.dto.ApiResponse;
import com.ecommerce.user_service.common.exception.ApiException;
import com.ecommerce.user_service.user.dto.UserProfileRegisterRequest;
import com.ecommerce.user_service.user.dto.UserProfileRegisterResponse;
import com.ecommerce.user_service.user.entity.UserProfile;
import com.ecommerce.user_service.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImp implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    @Override
    public UserProfileRegisterResponse register(UserProfileRegisterRequest request, UUID userId) {
        if (userProfileRepository.existsById(userId))
            throw new ApiException("User profile already exists.", HttpStatus.CONFLICT);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.UNAUTHORIZED));

        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .fullName(request.fullName())
                .phoneNumber(request.phoneNumber())
                .address(request.address())
                .build();

        userProfileRepository.save(userProfile);

        return new UserProfileRegisterResponse(userProfile.getId());
    }
}
