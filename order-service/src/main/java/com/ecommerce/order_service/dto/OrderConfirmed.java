package com.ecommerce.order_service.dto;

import java.util.UUID;

public record OrderConfirmed(UUID orderId, UUID userId) {
}
