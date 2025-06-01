package com.ecommerce.inventory_service.dto;

import java.util.UUID;

public record StockStatusUpdatedEvent(
        UUID productId,
        boolean inStock
) { }
