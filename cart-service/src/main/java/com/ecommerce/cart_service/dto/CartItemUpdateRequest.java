package com.ecommerce.cart_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record CartItemUpdateRequest(
        @NotNull
        @PositiveOrZero
        Integer quantity
) { }
