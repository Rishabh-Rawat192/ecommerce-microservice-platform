package com.ecommerce.inventory_service.entity;

import jakarta.persistence.*;
import lombok.*;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation_items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ReservationItem {
    @EmbeddedId
    private ReservationItemId id;

    @Column(nullable = false)
    private Integer reservedQuantity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onSave() {
        createdAt = LocalDateTime.now();
    }
}
