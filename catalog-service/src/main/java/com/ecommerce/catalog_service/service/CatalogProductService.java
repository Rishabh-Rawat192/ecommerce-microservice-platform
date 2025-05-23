package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.ProductFilterRequest;
import com.ecommerce.catalog_service.dto.ProductResponse;
import com.ecommerce.catalog_service.entity.CatalogProduct;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface CatalogProductService {
    Page<ProductResponse> getProducts(ProductFilterRequest request);
    ProductResponse getById(UUID productId);
}
