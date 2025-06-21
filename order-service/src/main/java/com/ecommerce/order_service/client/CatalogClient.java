package com.ecommerce.order_service.client;

import com.ecommerce.order_service.dto.ApiResponse;
import com.ecommerce.order_service.dto.ProductPriceResponse;
import com.ecommerce.order_service.dto.ProductsPriceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "catalog-service", url = "${catalog.service.url}")
public interface CatalogClient {

    @PostMapping("/internal/catalog/products/prices")
    ApiResponse<List<ProductPriceResponse>> getProductsPrice(@RequestBody ProductsPriceRequest request);
}
