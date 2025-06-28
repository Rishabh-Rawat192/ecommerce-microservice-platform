package com.ecommerce.order_service.dto;

import java.util.UUID;

public record OrderCancelledEvent(UUID orderId, UUID userId) {
}
