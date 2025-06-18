package com.ecommerce.order_service.dto;


import java.util.UUID;

public record CartItemResponse(
        UUID id,
        UUID productId,
        Integer quantity
) { }
