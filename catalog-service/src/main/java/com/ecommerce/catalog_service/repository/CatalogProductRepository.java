package com.ecommerce.catalog_service.repository;

import com.ecommerce.catalog_service.entity.CatalogProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface CatalogProductRepository extends JpaRepository<CatalogProduct, UUID>, JpaSpecificationExecutor<CatalogProduct> {
}
