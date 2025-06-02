package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.dto.StockResponse;
import com.ecommerce.inventory_service.dto.StockUpdateRequest;

import java.util.UUID;

public interface StockService {
    StockResponse restock(StockUpdateRequest request, UUID productId, UUID sellerId);
    StockResponse deductStock(StockUpdateRequest request, UUID productId, UUID sellerId);
    StockResponse getStock(UUID productId);
}
