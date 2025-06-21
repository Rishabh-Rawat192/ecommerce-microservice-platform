package com.ecommerce.order_service.dto;


import java.math.BigDecimal;
import java.util.UUID;

public record ProductPriceResponse(
        UUID productId,
        BigDecimal price,
        boolean found
) { }
