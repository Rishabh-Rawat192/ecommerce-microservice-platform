package com.ecommerce.catalog_service.dto;

import com.ecommerce.catalog_service.entity.CatalogProduct;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        UUID sellerId,
        String name,
        String description,
        String category,
        String brand,
        String imageUrl,
        boolean inStock,
        BigDecimal price
)
{
    public static ProductResponse from(CatalogProduct product) {
        return new ProductResponse(
                product.getId(),
                product.getSellerId(),
                product.getName(),
                product.getDescription(),
                product.getCategory(),
                product.getBrand(),
                product.getImageUrl(),
                product.isInStock(),
                product.getPrice()
        );
    }
}
