package com.ecommerce.product_service.dto;

import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.repository.ProductRepository;
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
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getSellerId(), product.getName(), product.getDescription(),
                product.getPrice(), product.getCategory(), product.getBrand(), product.getImageUrl());
    }
}
