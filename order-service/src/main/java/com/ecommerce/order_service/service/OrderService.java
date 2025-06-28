package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.OrderCreateResponse;
import com.ecommerce.order_service.dto.OrderResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderCreateResponse createOrder(UUID userId);
    OrderResponse confirmOrder(UUID orderId, UUID userId);
    OrderResponse cancelOrder(UUID orderId, UUID userId);
    void expireStaleOrders(LocalDateTime expirationTime);

    OrderResponse getOrder(UUID orderId, UUID userId);
    List<OrderResponse> getAllOrders(UUID userId);
}
