package com.ecommerce.catalog_service.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductUpdatedEvent(
    @NotNull(message = "Product ID cannot be null")
    UUID productId,
    String name,
    String description,
    BigDecimal price,
    String category,
    String brand,
    String imageUrl
) { }
