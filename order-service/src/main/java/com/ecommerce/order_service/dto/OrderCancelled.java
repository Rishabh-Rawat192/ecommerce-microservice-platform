package com.ecommerce.order_service.dto;

import java.util.UUID;

public record OrderCancelled(UUID orderId, UUID userId) {
}
