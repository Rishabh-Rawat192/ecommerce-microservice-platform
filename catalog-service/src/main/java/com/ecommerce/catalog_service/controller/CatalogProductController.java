package com.ecommerce.catalog_service.controller;

import com.ecommerce.catalog_service.dto.ApiResponse;
import com.ecommerce.catalog_service.dto.PagedResponse;
import com.ecommerce.catalog_service.dto.ProductFilterRequest;
import com.ecommerce.catalog_service.dto.ProductResponse;
import com.ecommerce.catalog_service.service.CatalogProductService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.shaded.com.google.protobuf.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/catalog/products")
@RequiredArgsConstructor
public class CatalogProductController {

    private static final Logger logger = LogManager.getLogger(CatalogProductController.class);

    private final CatalogProductService catalogProductService;

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<ProductResponse>>> getProducts(@ModelAttribute @Valid ProductFilterRequest request) {
        logger.info("Received request to get products with filter: {}", request);
        PagedResponse<ProductResponse> response = catalogProductService.getProducts(request);
        return ResponseEntity.ok(ApiResponse.success("Successfully found products.", response));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable UUID productId) {
        logger.info("Received request to get product with id: {}", productId);
        ProductResponse response = catalogProductService.getById(productId);
        return ResponseEntity.ok(ApiResponse.success("Successfully found the product.", response));
    }
}
