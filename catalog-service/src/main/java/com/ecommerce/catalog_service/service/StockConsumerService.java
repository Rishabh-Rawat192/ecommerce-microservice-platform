package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.StockStatusUpdatedEvent;

public interface StockConsumerService {
    void consumeStockStatusUpdatedEvent(String event);
}
