package com.ecommerce.order_service.dto;

import java.util.UUID;

public record OrderConfirmationFailed(UUID orderId, UUID userId) {
}
