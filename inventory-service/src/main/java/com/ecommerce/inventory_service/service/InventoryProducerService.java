package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.dto.StockStatusUpdatedEvent;

public interface InventoryProducerService {
    void sendStockStatusUpdatedEvent(StockStatusUpdatedEvent event);
}
