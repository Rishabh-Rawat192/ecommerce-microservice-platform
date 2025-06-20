package com.ecommerce.order_service.dto;

import com.ecommerce.order_service.entity.OrderItem;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemCreateResponse(
        UUID productId,
        BigDecimal price,
        BigDecimal totalPrice,
        int requestedQuantity,
        int reservedQuantity
) {
    public static OrderItemCreateResponse from(ReserveStockItemResponse item, BigDecimal price, BigDecimal totalPrice) {
        return new OrderItemCreateResponse(item.productId(), price, totalPrice,
                item.requestedQuantity(), item.reservedQuantity());
    }
}
