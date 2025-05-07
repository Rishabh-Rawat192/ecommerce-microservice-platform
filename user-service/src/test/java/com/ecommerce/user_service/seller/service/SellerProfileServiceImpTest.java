package com.ecommerce.user_service.seller.service;

import com.ecommerce.user_service.auth.UserRepository;
import com.ecommerce.user_service.auth.entity.User;
import com.ecommerce.user_service.common.enums.Role;
import com.ecommerce.user_service.common.exception.ApiException;
import com.ecommerce.user_service.seller.dto.SellerProfileRegisterRequest;
import com.ecommerce.user_service.seller.dto.SellerProfileUpdateRequest;
import com.ecommerce.user_service.seller.entity.SellerProfile;
import com.ecommerce.user_service.seller.repository.SellerProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class SellerProfileServiceImpTest {

    private SellerProfileRepository sellerProfileRepository;
    private UserRepository userRepository;
    private SellerProfileServiceImp sellerProfileService;

    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        sellerProfileRepository = mock(SellerProfileRepository.class);
        userRepository = mock(UserRepository.class);
        sellerProfileService = new SellerProfileServiceImp(sellerProfileRepository, userRepository);
    }

    @Test
    void register_shouldCreateSellerProfile_whenValidRequest() {
        SellerProfileRegisterRequest request = new SellerProfileRegisterRequest("  My Shop  ", "GST123", "1234567890", "  Address  ");
        User user = User.builder().id(userId).role(Role.USER).build();

        when(sellerProfileRepository.existsById(userId)).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        var response = sellerProfileService.register(request, userId);

        ArgumentCaptor<SellerProfile> sellerProfileCaptor = ArgumentCaptor.forClass(SellerProfile.class);
        verify(sellerProfileRepository).save(sellerProfileCaptor.capture());
        verify(userRepository).save(user);

        SellerProfile savedProfile = sellerProfileCaptor.getValue();
        assertThat(savedProfile.getBusinessName()).isEqualTo("My Shop");
        assertThat(savedProfile.getAddress()).isEqualTo("1234567890");
        assertThat(response.userId()).isEqualTo(userId);
        assertThat(user.getRole()).isEqualTo(Role.SELLER);
    }

    @Test
    void register_shouldThrow_whenProfileExists() {
        when(sellerProfileRepository.existsById(userId)).thenReturn(true);
        SellerProfileRegisterRequest request = new SellerProfileRegisterRequest("Shop", "GST", "123", "Address");

        assertThatThrownBy(() -> sellerProfileService.register(request, userId))
                .isInstanceOf(ApiException.class)
                .hasMessage("Seller Profile already exists.")
                .extracting("httpStatus")
                .isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void register_shouldThrow_whenUserNotFound() {
        when(sellerProfileRepository.existsById(userId)).thenReturn(false);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        SellerProfileRegisterRequest request = new SellerProfileRegisterRequest("Shop", "GST", "123", "Address");

        assertThatThrownBy(() -> sellerProfileService.register(request, userId))
                .isInstanceOf(ApiException.class)
                .hasMessage("User not found.")
                .extracting("httpStatus")
                .isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void get_shouldReturnSellerProfile_whenExists() {
        SellerProfile profile = SellerProfile.builder()
                .user(User.builder().id(userId).build())
                .businessName("Biz")
                .gstNumber("GSTIN")
                .phoneNumber("1234567890")
                .address("Addr")
                .build();

        when(sellerProfileRepository.findById(userId)).thenReturn(Optional.of(profile));

        var response = sellerProfileService.get(userId);

        assertThat(response.businessName()).isEqualTo("Biz");
        assertThat(response.gstNumber()).isEqualTo("GSTIN");
        assertThat(response.address()).isEqualTo("Addr");
        assertThat(response.phoneNumber()).isEqualTo("1234567890");
    }

    @Test
    void get_shouldThrow_whenProfileNotFound() {
        when(sellerProfileRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sellerProfileService.get(userId))
                .isInstanceOf(ApiException.class)
                .hasMessage("Seller Profile not found.")
                .extracting("httpStatus")
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void update_shouldUpdateOnlyNonBlankFields() {
        SellerProfile profile = SellerProfile.builder()
                .user(User.builder().id(userId).build())
                .businessName("Old Name")
                .gstNumber("OLDGST")
                .phoneNumber("000")
                .address("Old Address")
                .build();

        when(sellerProfileRepository.findById(userId)).thenReturn(Optional.of(profile));

        SellerProfileUpdateRequest updateRequest = new SellerProfileUpdateRequest(
                "New Name", null, "New Address", "1234567890"
        );

        var response = sellerProfileService.update(updateRequest, userId);

        assertThat(response.businessName()).isEqualTo("New Name");
        assertThat(response.address()).isEqualTo("New Address");
        assertThat(response.phoneNumber()).isEqualTo("1234567890");
        assertThat(response.gstNumber()).isEqualTo("OLDGST");
    }

    @Test
    void update_shouldThrow_whenProfileNotFound() {
        when(sellerProfileRepository.findById(userId)).thenReturn(Optional.empty());

        SellerProfileUpdateRequest updateRequest = new SellerProfileUpdateRequest("Biz", "GST", "Addr", "123");

        assertThatThrownBy(() -> sellerProfileService.update(updateRequest, userId))
                .isInstanceOf(ApiException.class)
                .hasMessage("Seller Profile not found.")
                .extracting("httpStatus")
                .isEqualTo(HttpStatus.NOT_FOUND);
    }
}
