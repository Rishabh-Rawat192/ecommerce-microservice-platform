package com.ecommerce.order_service.dto;

import java.util.UUID;

public record OrderConfirmationFailedEvent(UUID orderId, UUID userId) {
}
