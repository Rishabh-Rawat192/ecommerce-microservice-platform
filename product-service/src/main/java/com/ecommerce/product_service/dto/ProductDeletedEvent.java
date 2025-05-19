package com.ecommerce.product_service.dto;

import com.ecommerce.product_service.entity.Product;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDeletedEvent(
        UUID productId
) {
    public static ProductDeletedEvent from(Product product) {
        return new ProductDeletedEvent(
                product.getId()
        );
    }
}
