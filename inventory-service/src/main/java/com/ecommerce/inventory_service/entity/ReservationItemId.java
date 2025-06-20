package com.ecommerce.inventory_service.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReservationItemId implements Serializable {
    private UUID orderId;
    private UUID productId;
}
