package com.ecommerce.catalog_service.dto;

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
) { }
