package com.ecommerce.cart_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record CartItemResponse(
        UUID id,
        UUID productId,
        Integer quantity
) { }
