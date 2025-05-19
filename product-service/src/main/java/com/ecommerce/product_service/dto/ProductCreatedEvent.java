package com.ecommerce.product_service.dto;

import com.ecommerce.product_service.entity.Product;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductCreatedEvent(
        UUID productId,
        UUID sellerId,
        String name,
        String description,
        BigDecimal price,
        String category,
        String brand,
        String imageUrl
) {
    public static ProductCreatedEvent from(Product product) {
        return new ProductCreatedEvent(
                product.getId(),
                product.getSellerId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getBrand(),
                product.getImageUrl()
        );
    }
}
