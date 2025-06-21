package com.ecommerce.order_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record CartItemWithPrice(
        UUID productId,
        int quantity,
        BigDecimal price
) {
    public static CartItemWithPrice from(CartItemResponse cartItemResponse, BigDecimal price) {
        return new CartItemWithPrice(cartItemResponse.productId(), cartItemResponse.quantity(), price);
    }
}
