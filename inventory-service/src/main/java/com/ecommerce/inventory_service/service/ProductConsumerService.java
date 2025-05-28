package com.ecommerce.inventory_service.service;

public interface ProductConsumerService {
    void consumeProductCreatedEvent(String productCreatedEvent);
}
