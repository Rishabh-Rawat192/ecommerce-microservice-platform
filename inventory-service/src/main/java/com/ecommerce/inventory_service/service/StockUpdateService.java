package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.dto.StockUpdateRequest;
import com.ecommerce.inventory_service.dto.StockUpdateResponse;

import java.util.UUID;

public interface StockUpdateService {
    StockUpdateResponse restock(StockUpdateRequest request, UUID productId, UUID sellerId);
    StockUpdateResponse deductStock(StockUpdateRequest request, UUID productId, UUID sellerId);
}
