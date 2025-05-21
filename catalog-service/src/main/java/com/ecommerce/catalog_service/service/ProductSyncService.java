package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.ProductCreatedEvent;
import com.ecommerce.catalog_service.dto.ProductDeletedEvent;
import com.ecommerce.catalog_service.dto.ProductUpdatedEvent;

public interface ProductSyncService {
    void createProduct(ProductCreatedEvent event);
    void updateProduct(ProductUpdatedEvent event);
    void deleteProduct(ProductDeletedEvent event);
}
