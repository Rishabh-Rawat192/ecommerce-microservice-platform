package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.ProductRegisterRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.dto.ProductUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductResponse register(ProductRegisterRequest request, UUID sellerId);
    ProductResponse get(UUID productId);
    ProductResponse update(ProductUpdateRequest request, UUID productId, UUID sellerId);
    void delete(UUID productId, UUID sellerId);
    List<ProductResponse> getAllBySellerId(UUID sellerId);
}
