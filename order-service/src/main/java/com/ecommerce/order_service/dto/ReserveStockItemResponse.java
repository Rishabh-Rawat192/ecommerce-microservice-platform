package com.ecommerce.order_service.dto;

import com.ecommerce.order_service.enums.ReservationItemStatus;

import java.util.UUID;

public record ReserveStockItemResponse(
        UUID productId,
        int requestedQuantity,
        int reservedQuantity,
        ReservationItemStatus status
) { }
