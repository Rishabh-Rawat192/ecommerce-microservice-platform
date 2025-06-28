package com.ecommerce.cart_service.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderConfirmedEvent(@NotNull UUID userId) {
}
