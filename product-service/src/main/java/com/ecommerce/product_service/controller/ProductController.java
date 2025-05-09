package com.ecommerce.product_service.controller;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @GetMapping
    public ResponseEntity<List<String>> getAllProducts() {
        List<String> products = Arrays.asList("Product1", "Product2");
        return ResponseEntity.ok(products);
    }
}
