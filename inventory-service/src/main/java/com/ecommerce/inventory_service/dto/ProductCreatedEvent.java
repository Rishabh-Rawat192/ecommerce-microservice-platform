package com.ecommerce.inventory_service.dto;


import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductCreatedEvent(
        @NotNull(message = "Product Id cannot be null")
        UUID productId,
        @NotNull(message = "Seller ID cannot be null")
        UUID sellerId
) { }
