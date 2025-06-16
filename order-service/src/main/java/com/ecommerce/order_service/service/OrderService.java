package com.ecommerce.order_service.service;

import com.ecommerce.order_service.dto.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponse createOrder(UUID userId);
    OrderResponse confirmOrder(UUID orderId, UUID userId);
    OrderResponse cancelOrder(UUID orderId, UUID userId);
    OrderResponse getOrder(UUID orderId, UUID userId);
    List<OrderResponse> getAllOrders(UUID userId);
}
