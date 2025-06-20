package com.ecommerce.order_service.dto;

import com.ecommerce.order_service.entity.OrderItem;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID productId,
        BigDecimal price,
        BigDecimal totalPrice,
        int quantity
) {
    public static OrderItemResponse from (OrderItem item) {
        return new OrderItemResponse(item.getProductId(), item.getPrice(),
                item.getTotalPrice(), item.getQuantity());
    }
}
