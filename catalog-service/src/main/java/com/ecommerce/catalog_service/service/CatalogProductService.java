package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.*;
import com.ecommerce.catalog_service.entity.CatalogProduct;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface CatalogProductService {
    PagedResponse<ProductResponse> getProducts(ProductFilterRequest request);
    ProductResponse getById(UUID productId);
    boolean productExists(UUID productId);
    List<ProductPriceResponse> getProductsPrice(ProductsPriceRequest request);
}
