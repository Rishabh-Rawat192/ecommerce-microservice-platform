package com.ecommerce.order_service.dto;

import com.ecommerce.order_service.enums.Currency;
import com.ecommerce.order_service.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderCreateResponse(
        UUID orderId,
        List<OrderItemCreateResponse> items,
        BigDecimal totalPrice,
        Currency currency,
        OrderStatus orderStatus
) { }
