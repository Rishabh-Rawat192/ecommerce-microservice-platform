package com.ecommerce.inventory_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record StockUpdateRequest(
        @NotNull(message = "Stock is required")
        @Positive
        Integer stock
) { }
