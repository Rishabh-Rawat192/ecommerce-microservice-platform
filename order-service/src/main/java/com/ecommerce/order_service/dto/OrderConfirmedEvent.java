package com.ecommerce.order_service.dto;

import java.util.UUID;

public record OrderConfirmedEvent(UUID orderId, UUID userId) {
}
