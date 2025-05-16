package com.ecommerce.product_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "active_seller_ids")
@AllArgsConstructor
@NoArgsConstructor
public class ActiveSeller {
    @Id
    private UUID sellerId;
}
