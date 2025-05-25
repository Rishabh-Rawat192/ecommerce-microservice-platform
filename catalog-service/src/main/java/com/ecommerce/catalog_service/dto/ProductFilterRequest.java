package com.ecommerce.catalog_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductFilterRequest(
        String name,
        String description,
        String category,
        String brand,
        UUID sellerId,

        @PositiveOrZero(message = "Minimum price must be zero or positive")
        BigDecimal minPrice,
        @PositiveOrZero(message = "Maximum price must be zero or positive")
        BigDecimal maxPrice,
        Boolean inStock,

        String sortBy,
        String sortDirection,

        @PositiveOrZero(message = "Page number must be zero or positive")
        Integer page,
        @Min(value = 1, message = "Page size must be at least 1")
        Integer size
) { }
