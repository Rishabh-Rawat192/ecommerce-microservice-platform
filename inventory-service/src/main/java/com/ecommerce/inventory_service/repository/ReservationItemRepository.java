package com.ecommerce.inventory_service.repository;

import com.ecommerce.inventory_service.entity.ReservationItem;
import com.ecommerce.inventory_service.entity.ReservationItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReservationItemRepository extends JpaRepository<ReservationItem, ReservationItemId> {
    boolean existsByIdOrderId(UUID orderId);
}
