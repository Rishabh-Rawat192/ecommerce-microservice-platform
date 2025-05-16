package com.ecommerce.product_service.repository;

import com.ecommerce.product_service.entity.ActiveSeller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ActiveSellerRepository extends JpaRepository<ActiveSeller, UUID> {
}
