package com.ecommerce.order_service.dto;

import com.ecommerce.order_service.entity.Order;
import com.ecommerce.order_service.enums.Currency;
import com.ecommerce.order_service.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record OrderResponse(
        UUID orderId,
        List<OrderItemResponse> items,
        BigDecimal totalPrice,
        Currency currency,
        OrderStatus orderStatus
) {
    public static OrderResponse from(Order order) {
        List<OrderItemResponse> orderItemResponses = order.getItems().stream()
                .map(OrderItemResponse::from)
                .toList();

        return new OrderResponse(order.getId(), orderItemResponses, order.getTotalPrice(), order.getCurrency(), order.getOrderStatus());
    }
}
