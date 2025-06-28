package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.dto.*;

import java.util.List;
import java.util.UUID;

public interface StockService {
    StockResponse restock(StockUpdateRequest request, UUID productId, UUID sellerId);
    StockResponse deductStock(StockUpdateRequest request, UUID productId, UUID sellerId);
    StockResponse getStock(UUID productId);

    List<ReserveStockItemResponse> createReservation(ReserveStockRequest request);
    void confirmReservation(UUID orderId);

    void cancelReservation(OrderCancelledEvent event);
    void expireReservation(OrderExpiredEvent event);
    void rollbackReservation(OrderCreationFailedEvent event);
    void rollbackReservationConfirmation(OrderConfirmationFailedEvent event);
}
