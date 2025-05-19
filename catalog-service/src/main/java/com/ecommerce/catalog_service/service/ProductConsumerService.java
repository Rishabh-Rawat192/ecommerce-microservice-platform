package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.ProductCreatedEvent;
import org.apache.kafka.clients.consumer.Consumer;

public interface ProductConsumerService {
    void consumeProductCreatedEvent(String productCreatedEvent);
    void consumeProductUpdatedEvent(String productCreatedEvent);
    void consumeProductDeletedEvent(String productUpdatedEvent);
}
