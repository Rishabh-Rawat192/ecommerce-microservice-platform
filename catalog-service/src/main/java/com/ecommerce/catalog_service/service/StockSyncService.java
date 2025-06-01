package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.StockStatusUpdatedEvent;

public interface StockSyncService {
    void updateStockStatus(StockStatusUpdatedEvent event);
}
