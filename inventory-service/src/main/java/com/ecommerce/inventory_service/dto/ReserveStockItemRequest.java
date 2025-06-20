package com.ecommerce.inventory_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReserveStockItemRequest(
        @NotNull
        UUID productId,
        @NotNull
        @Min(1)
        Integer quantity
) { }
