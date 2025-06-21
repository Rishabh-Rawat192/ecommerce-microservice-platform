package com.ecommerce.catalog_service.controller;

import com.ecommerce.catalog_service.dto.ApiResponse;
import com.ecommerce.catalog_service.dto.ProductPriceResponse;
import com.ecommerce.catalog_service.dto.ProductsPriceRequest;
import com.ecommerce.catalog_service.service.CatalogProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal/catalog/products")
@RequiredArgsConstructor
public class InternalCatalogProductController {

    private static final Logger logger = LogManager.getLogger(InternalCatalogProductController.class);
    private final CatalogProductService catalogProductService;

    @RequestMapping(path = "/{productId}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> productExists(@PathVariable UUID productId) {
        logger.info("Request received to check if product exits with ID: {}", productId);
        boolean exists = catalogProductService.productExists(productId);

        if (exists) {
            logger.info("Product exits with ID: {}", productId);
            return ResponseEntity.noContent().build();
        } else {
            logger.info("Product does not exists with ID: {}", productId);
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/prices")
    public ResponseEntity<ApiResponse<List<ProductPriceResponse>>> getProductsPrice(
            @Valid @RequestBody ProductsPriceRequest request) {
        logger.info("Request received to get products price.");

        List<ProductPriceResponse> responses = catalogProductService.getProductsPrice(request);
        return ResponseEntity.ok(ApiResponse.success("Successfully fetched products price.", responses));
    }
}
