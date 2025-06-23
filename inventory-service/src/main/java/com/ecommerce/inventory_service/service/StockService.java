package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.dto.ReserveStockItemResponse;
import com.ecommerce.inventory_service.dto.ReserveStockRequest;
import com.ecommerce.inventory_service.dto.StockResponse;
import com.ecommerce.inventory_service.dto.StockUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface StockService {
    StockResponse restock(StockUpdateRequest request, UUID productId, UUID sellerId);
    StockResponse deductStock(StockUpdateRequest request, UUID productId, UUID sellerId);
    StockResponse getStock(UUID productId);

    List<ReserveStockItemResponse> createReservation(ReserveStockRequest request);
    void confirmReservation(UUID orderId);
    void cancelReservation(UUID orderId);
}
