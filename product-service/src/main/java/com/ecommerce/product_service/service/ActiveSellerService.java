package com.ecommerce.product_service.service;

import java.util.UUID;

public interface ActiveSellerService {
    void save(UUID sellerId);
    void delete(UUID sellerId);
    boolean isActive(UUID sellerId);
}
