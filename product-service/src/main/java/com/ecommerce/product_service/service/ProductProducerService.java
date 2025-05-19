package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.ProductCreatedEvent;
import com.ecommerce.product_service.dto.ProductDeletedEvent;
import com.ecommerce.product_service.dto.ProductUpdatedEvent;

import java.util.UUID;

public interface ProductProducerService {
    void sendProductCreationEvent(UUID productId, ProductCreatedEvent event);
    void sendProductUpdateEvent(UUID productId, ProductUpdatedEvent event);
    void sendProductDeletionEvent(UUID productId, ProductDeletedEvent event);
}
