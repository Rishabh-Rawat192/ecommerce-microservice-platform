package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.*;

import java.util.UUID;

public interface OrderProducerService {
    void sendOrderCreationFailedEvent(UUID orderId, OrderCreationFailed event);
    void sendOrderConfirmedEvent(UUID orderId, OrderConfirmed event);
    void sendOrderConfirmationFailedEvent(UUID orderId, OrderConfirmationFailed event);
    void sendOrderCancelledEvent(UUID orderId, OrderCancelled event);
    void sendOrderExpiredEvent(UUID orderId, OrderExpiredEvent event);
}
