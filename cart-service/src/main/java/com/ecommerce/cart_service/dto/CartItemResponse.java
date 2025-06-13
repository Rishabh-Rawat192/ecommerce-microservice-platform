package com.ecommerce.cart_service.dto;

import com.ecommerce.cart_service.entity.CartItem;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record CartItemResponse(
        UUID id,
        UUID productId,
        Integer quantity
) {
    public static CartItemResponse from(CartItem item) {
        return new CartItemResponse(item.getId(), item.getProductId(), item.getQuantity());
    }
}
