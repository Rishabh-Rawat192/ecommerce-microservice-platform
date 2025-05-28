package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.dto.ProductCreatedEvent;

public interface InventoryService {
    void createProduct(ProductCreatedEvent event);
}
