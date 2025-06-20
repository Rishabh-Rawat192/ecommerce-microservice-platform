package com.ecommerce.order_service.dto;

import java.util.List;
import java.util.UUID;

public record ReserveStockRequest(
        UUID orderId,
        List<ReserveStockItemRequest> items
) {
    public static ReserveStockRequest from(UUID orderId, List<CartItemResponse> cartItemResponses) {
        List<ReserveStockItemRequest> reserveStockItemRequests = cartItemResponses.stream()
                .map(ReserveStockItemRequest::from)
                .toList();

        return new ReserveStockRequest(orderId, reserveStockItemRequests);
    }
}