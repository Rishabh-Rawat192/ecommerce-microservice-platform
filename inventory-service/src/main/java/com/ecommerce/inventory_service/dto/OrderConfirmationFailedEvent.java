package com.ecommerce.inventory_service.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderConfirmationFailedEvent(@NotNull UUID orderId) {
}
