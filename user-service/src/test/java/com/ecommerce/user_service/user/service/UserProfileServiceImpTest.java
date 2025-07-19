package com.ecommerce.user_service.user.service;


import com.ecommerce.user_service.auth.UserRepository;
import com.ecommerce.user_service.auth.entity.User;
import com.ecommerce.user_service.common.exception.ApiException;
import com.ecommerce.user_service.user.dto.UserProfileRegisterRequest;
import com.ecommerce.user_service.user.dto.UserProfileRegisterResponse;
import com.ecommerce.user_service.user.dto.UserProfileResponse;
import com.ecommerce.user_service.user.dto.UserProfileUpdateRequest;
import com.ecommerce.user_service.user.entity.UserProfile;
import com.ecommerce.user_service.user.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceImpTest {
    private UserProfileService userProfileService;
    private UserProfileRepository userProfileRepository;
    private UserRepository userRepository;

     @BeforeEach
     void setup() {
         // Initialize other dependencies and mocks as needed
         // For example, you can use Mockito to create mocks for UserProfileRepository and UserRepository
         userProfileRepository = mock(UserProfileRepository.class);
         userRepository = mock(UserRepository.class);

         userProfileService = new UserProfileServiceImp(userProfileRepository, userRepository);
     }

    @Test
    void register_shouldSaveProfile_whenUserExistsAndNoProfileExists() {
        // Arrange
        UUID userId = UUID.randomUUID();
        User user = User.builder().build();
        user.setId(userId);

        UserProfileRegisterRequest request = new UserProfileRegisterRequest(
                "  John Doe  ",
                "9876543210",
                "  123 Main Street  "
        );

        when(userProfileRepository.existsById(userId)).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userProfileRepository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> {
                    UserProfile userProfile = invocation.getArgument(0);
                    userProfile.setId(userId); // Mock the explicit setting of the ID
                    return userProfile;
                });

        // Act
        UserProfileRegisterResponse response = userProfileService.register(request, userId);

        // Assert
        assertNotNull(response);
        assertEquals(userId, response.userId());

        ArgumentCaptor<UserProfile> captor = ArgumentCaptor.forClass(UserProfile.class);
        verify(userProfileRepository).save(captor.capture());

        UserProfile savedProfile = captor.getValue();
        assertEquals(user, savedProfile.getUser());
        assertEquals("John Doe", savedProfile.getFullName()); // Should be trimmed
        assertEquals("9876543210", savedProfile.getPhoneNumber());
        assertEquals("123 Main Street", savedProfile.getAddress());
    }

    @Test
    void register_shouldThrowException_whenProfileAlreadyExists() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserProfileRegisterRequest request = new UserProfileRegisterRequest(
                "Jane Doe", "1234567890", "456 Another St"
        );

        when(userProfileRepository.existsById(userId)).thenReturn(true);

        // Act + Assert
        ApiException exception = assertThrows(ApiException.class, () -> userProfileService.register(request, userId));
        assertEquals("User profile already exists.", exception.getMessage());
        assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus()); // Conflict
    }

    @Test
    void register_shouldThrowException_whenUserDoesNotExist() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserProfileRegisterRequest request = new UserProfileRegisterRequest(
                "Jane Doe", "1234567890", "456 Another St"
        );

        when(userProfileRepository.existsById(userId)).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act + Assert
        ApiException exception = assertThrows(ApiException.class, () -> userProfileService.register(request, userId));
        assertEquals("User not found", exception.getMessage());
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getHttpStatus()); // Unauthorized
    }

    @Test
    void get_shouldReturnProfile_whenProfileExists() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .build();

        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .fullName("John Doe")
                .phoneNumber("1234567890")
                .address("123 Main Street")
                .build();

        when(userProfileRepository.findById(userId)).thenAnswer((invocation) -> {
            userProfile.setId(userId); // Mock the automatic setting of the ID
            return Optional.of(userProfile);
        });

        UserProfileResponse response = userProfileService.get(userId);

        assertNotNull(response);
        assertEquals(userId, response.userId());
        assertEquals("John Doe", response.fullName());
        assertEquals("1234567890", response.phoneNumber());
        assertEquals("123 Main Street", response.address());

        verify(userProfileRepository).findById(userId);
    }

    @Test
    void get_shouldThrowException_whenProfileDoesNotExist() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .build();

        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .fullName("John Doe")
                .phoneNumber("1234567890")
                .address("123 Main Street")
                .build();

        when(userProfileRepository.findById(userId)).thenReturn(Optional.empty());

        ApiException ex = assertThrows(ApiException.class, () -> userProfileService.get(userId));
        assertEquals("User profile not found.", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());

        verify(userProfileRepository).findById(userId);
    }

    @Test
    void update_shouldUpdateProfile_whenProfileExists() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .build();

        UserProfile existingProfile = UserProfile.builder()
                .user(user)
                .fullName("Old Name")
                .phoneNumber("0000000000")
                .address("Old Address")
                .build();

        when(userProfileRepository.findById(userId)).thenReturn(Optional.of(existingProfile));

        UserProfileUpdateRequest updateRequest = new UserProfileUpdateRequest(
                "  New Name  ",
                "9999999999",
                "  New Address  "
        );

        userProfileService.update(updateRequest, userId);

        assertEquals("New Name", existingProfile.getFullName());
        assertEquals("9999999999", existingProfile.getPhoneNumber());
        assertEquals("New Address", existingProfile.getAddress());

        verify(userProfileRepository).findById(userId);
        verify(userProfileRepository).save(existingProfile);
    }

    @Test
    void update_shouldThrowException_whenProfileDoesNotExist() {
        UUID userId = UUID.randomUUID();

        when(userProfileRepository.findById(userId)).thenReturn(Optional.empty());

        UserProfileUpdateRequest updateRequest = new UserProfileUpdateRequest(
                "New Name",
                "9999999999",
                "New Address"
        );

        ApiException ex = assertThrows(ApiException.class, () -> userProfileService.update(updateRequest, userId));
        assertEquals("User profile not found.", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());

        verify(userProfileRepository).findById(userId);
        verify(userProfileRepository, never()).save(any());
    }

}
