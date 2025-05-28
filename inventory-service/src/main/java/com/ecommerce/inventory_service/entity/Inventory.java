package com.ecommerce.inventory_service.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "inventory")
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Inventory {
    @Id
    private UUID productId;
    @Column(nullable = false)
    private UUID sellerId;

    @Column(nullable = false)
    private int totalQuantity;
    @Column(nullable = false)
    private int reservedQuantity;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
