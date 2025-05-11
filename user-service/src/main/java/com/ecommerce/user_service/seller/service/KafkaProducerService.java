package com.ecommerce.user_service.seller.service;

import java.util.UUID;

public interface KafkaProducerService {
    void sendSellerStatusUpdate(String topic, UUID sellerId, boolean isActive);
}
