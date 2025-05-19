package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.ProductCreatedEvent;

import java.util.UUID;

public interface ProductProducerService {
    void sendProductCreationEvent(UUID productId, ProductCreatedEvent event);
}
