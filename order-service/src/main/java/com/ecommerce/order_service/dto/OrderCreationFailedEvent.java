package com.ecommerce.order_service.dto;

import java.util.UUID;

public record OrderCreationFailedEvent(UUID orderId, UUID userId) {
}
