package com.ecommerce.order_service.dto;

import com.ecommerce.order_service.entity.OrderItem;
import com.ecommerce.order_service.enums.OrderItemStatus;
import com.ecommerce.order_service.enums.ReservationItemStatus;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemCreateResponse(
        UUID productId,
        BigDecimal price,
        BigDecimal totalPrice,
        int requestedQuantity,
        int reservedQuantity,
        OrderItemStatus orderItemStatus
) {
    public static OrderItemCreateResponse from(ReserveStockItemResponse item, BigDecimal price, BigDecimal totalPrice) {
        return new OrderItemCreateResponse(item.productId(), price, totalPrice,
                item.requestedQuantity(), item.reservedQuantity(),
                item.status() == ReservationItemStatus.RESERVED ? OrderItemStatus.RESERVED : OrderItemStatus.FAILED);
    }
}
