package com.ecommerce.inventory_service.dto;

import com.ecommerce.inventory_service.entity.Inventory;

import java.util.UUID;

public record StockResponse(
        UUID productId,
        int totalQuantity,
        int reservedQuantity
) {
    public static StockResponse from(Inventory inventory) {
        return new StockResponse(inventory.getProductId(),inventory.getTotalQuantity(), inventory.getReservedQuantity());
    }
}
