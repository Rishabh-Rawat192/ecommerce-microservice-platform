package com.ecommerce.inventory_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ReserveStockRequest(
        @NotNull
        UUID orderId,
        @NotNull
        @Valid List<ReserveStockItemRequest> items
) { }