package com.ecommerce.catalog_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductDeletedEvent(
    UUID productId
) { }
