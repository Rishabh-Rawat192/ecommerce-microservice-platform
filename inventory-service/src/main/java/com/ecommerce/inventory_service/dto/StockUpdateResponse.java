package com.ecommerce.inventory_service.dto;

import com.ecommerce.inventory_service.entity.Inventory;

import java.util.UUID;

public record StockUpdateResponse(
        UUID productId,
        int totalQuantity,
        int reservedQuantity
) {
    public static StockUpdateResponse from(Inventory inventory) {
        return new StockUpdateResponse(inventory.getProductId(),inventory.getTotalQuantity(), inventory.getReservedQuantity());
    }
}
