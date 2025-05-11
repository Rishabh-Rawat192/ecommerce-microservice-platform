package com.ecommerce.product_service.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductRegisterRequest(
        @NotBlank(message = "Product name is required")
        String name,
        @NotBlank(message = "Description is required")
        String description,
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        BigDecimal price,
        @NotBlank(message = "Category is required")
        String category,
        @NotBlank(message = "Brand is required")
        String brand,
        @NotBlank(message = "Image URL is required")
        String imageUrl
) {}
