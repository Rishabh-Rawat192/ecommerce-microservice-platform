package com.ecommerce.order_service.dto;

import com.ecommerce.order_service.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        List<OrderItemResponse> items,
        BigDecimal orderTotal,
        OrderStatus orderStatus
) { }
