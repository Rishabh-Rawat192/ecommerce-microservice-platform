package com.ecommerce.catalog_service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductCreatedEvent(
    @NotNull(message = "Product ID cannot be null")
    UUID productId,
    @NotNull(message = "Seller ID cannot be null")
    UUID sellerId,
    @NotEmpty(message = "Product name cannot be empty")
    String name,
    @NotEmpty(message = "Product description cannot be empty")
    String description,
    @NotNull(message = "Product price cannot be null")
    BigDecimal price,
    @NotEmpty(message = "Product category cannot be empty")
    String category,
    @NotEmpty(message = "Product brand cannot be empty")
    String brand,
    @NotEmpty(message = "Product image URL cannot be empty")
    String imageUrl
) { }
