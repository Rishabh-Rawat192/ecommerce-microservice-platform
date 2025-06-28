package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.*;

import java.util.UUID;

public interface OrderProducerService {
    void sendOrderCreationFailedEvent(UUID orderId, OrderCreationFailedEvent event);
    void sendOrderConfirmedEvent(UUID orderId, OrderConfirmedEvent event);
    void sendOrderConfirmationFailedEvent(UUID orderId, OrderConfirmationFailedEvent event);
    void sendOrderCancelledEvent(UUID orderId, OrderCancelledEvent event);
    void sendOrderExpiredEvent(UUID orderId, OrderExpiredEvent event);
}
