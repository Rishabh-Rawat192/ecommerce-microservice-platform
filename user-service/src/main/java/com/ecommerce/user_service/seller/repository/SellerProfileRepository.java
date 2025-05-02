package com.ecommerce.user_service.seller.repository;

import com.ecommerce.user_service.seller.entity.SellerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SellerProfileRepository extends JpaRepository<SellerProfile, UUID> {
}
