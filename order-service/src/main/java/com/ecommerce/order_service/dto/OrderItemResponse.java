package com.ecommerce.order_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID productId,
        BigDecimal price,
        BigDecimal totalPrice,
        int requestedQuantity,
        int reservedQuantity
) { }
