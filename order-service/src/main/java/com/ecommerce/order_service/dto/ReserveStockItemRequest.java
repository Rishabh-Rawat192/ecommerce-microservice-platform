package com.ecommerce.order_service.dto;

import java.util.UUID;

public record ReserveStockItemRequest(
        UUID productId,
        Integer quantity
) {
    public static ReserveStockItemRequest from(CartItemResponse cartItemResponse) {
        return new ReserveStockItemRequest(cartItemResponse.productId(), cartItemResponse.quantity());
    }
}
