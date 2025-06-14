package com.ecommerce.catalog_service.controller;

import com.ecommerce.catalog_service.service.CatalogProductService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
