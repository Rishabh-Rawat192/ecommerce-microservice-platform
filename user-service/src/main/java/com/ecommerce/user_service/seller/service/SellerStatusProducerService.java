package com.ecommerce.user_service.seller.service;

import java.util.UUID;

public interface SellerStatusProducerService {
    void sendSellerStatusUpdate(UUID sellerId, boolean isActive);
}
