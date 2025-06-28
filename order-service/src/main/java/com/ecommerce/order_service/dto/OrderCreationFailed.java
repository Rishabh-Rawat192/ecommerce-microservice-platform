package com.ecommerce.order_service.dto;

import java.util.UUID;

public record OrderCreationFailed(UUID orderId, UUID userId) {
}
