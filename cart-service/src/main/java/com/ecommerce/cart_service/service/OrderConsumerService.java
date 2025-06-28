package com.ecommerce.cart_service.service;

public interface OrderConsumerService {
    void consumeOrderConfirmedEvent(String orderConfirmedEvent);
}
