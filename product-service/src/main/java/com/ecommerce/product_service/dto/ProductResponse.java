package com.ecommerce.product_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        UUID sellerId,
        String name,
        String description,
        BigDecimal price,
        String category,
        String brand,
        String imageUrl
) { }
