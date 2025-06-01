package com.ecommerce.catalog_service.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record StockStatusUpdatedEvent(
    @NotNull(message = "ProductId can't be null.")
    UUID productId,
    @NotNull(message = "inStock can't be null.")
    Boolean inStock
) { }
