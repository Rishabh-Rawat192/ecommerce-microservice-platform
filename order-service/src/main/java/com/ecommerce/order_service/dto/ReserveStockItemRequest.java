package com.ecommerce.order_service.dto;

import java.util.UUID;

public record ReserveStockItemRequest(
        UUID productId,
        Integer quantity
) {
    public static ReserveStockItemRequest from(CartItemWithPrice cartItemWithPrice) {
        return new ReserveStockItemRequest(cartItemWithPrice.productId(), cartItemWithPrice.quantity());
    }
}
