package com.ecommerce.inventory_service.repository;

import com.ecommerce.inventory_service.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
}
