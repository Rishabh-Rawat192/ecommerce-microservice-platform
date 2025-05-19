package com.ecommerce.product_service.dto;

import com.ecommerce.product_service.entity.Product;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductUpdatedEvent(
        UUID productId,
        UUID sellerId,
        String name,
        String description,
        BigDecimal price,
        String category,
        String brand,
        String imageUrl
) {
    public static ProductUpdatedEvent from(Product product) {
        return new ProductUpdatedEvent(
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
