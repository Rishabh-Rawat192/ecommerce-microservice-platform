package com.ecommerce.inventory_service.service;

public interface OrderConsumerService {
    void consumeOrderCreationFailedEvent(String orderCreationFailedEvent);
    void consumeOrderConfirmationFailedEvent(String orderConfirmationFailedEvent);
    void consumeOrderCancelledEvent(String orderCancelledEvent);
    void consumeOrderExpiredEvent(String orderExpiredEvent);
}
