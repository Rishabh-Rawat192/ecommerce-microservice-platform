package com.ecommerce.cart_service.service;

import com.ecommerce.cart_service.dto.CreateCartItemRequest;
import com.ecommerce.cart_service.dto.CartItemResponse;
import com.ecommerce.cart_service.dto.UpdateCartItemRequest;

import java.util.List;
import java.util.UUID;

public interface CartService {
    List<CartItemResponse> getCart(UUID userId);
    void deleteCart(UUID userId);

    CartItemResponse getCartItem(UUID cartItemId, UUID userId);
    CartItemResponse createCartItem(CreateCartItemRequest request, UUID userId);
    CartItemResponse updateCartItem(UpdateCartItemRequest request, UUID cartItemId, UUID userId);
    void deleteCartItem(UUID cartItemId, UUID userId);
}
