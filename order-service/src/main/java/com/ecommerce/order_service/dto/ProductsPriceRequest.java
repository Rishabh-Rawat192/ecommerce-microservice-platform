package com.ecommerce.order_service.dto;

import java.util.List;
import java.util.UUID;

public record ProductsPriceRequest(
        List<UUID> productIds
) {
    public static ProductsPriceRequest from(List<CartItemResponse> cartItemResponses) {
        List<UUID> productIds = cartItemResponses.stream()
                .map(CartItemResponse::productId)
                .toList();

        return new ProductsPriceRequest(productIds);
    }
}
