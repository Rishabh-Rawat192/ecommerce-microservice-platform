package com.ecommerce.product_service.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.UUID;

public interface SellerStatusConsumerService {
    void consumeSellerStatusMessage(ConsumerRecord<UUID, String> record);
}
