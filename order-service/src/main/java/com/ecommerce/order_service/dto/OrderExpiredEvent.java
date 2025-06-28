package com.ecommerce.order_service.dto;

import java.util.UUID;

public record OrderExpiredEvent(UUID orderId, UUID userId) {
}
