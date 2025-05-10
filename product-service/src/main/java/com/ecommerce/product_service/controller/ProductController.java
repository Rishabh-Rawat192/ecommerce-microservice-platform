package com.ecommerce.product_service.controller;

import com.ecommerce.product_service.dto.UserDto;
import jakarta.annotation.security.RolesAllowed;
import lombok.NonNull;
import org.apache.coyote.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private static Logger logger = LogManager.getLogger(ProductController.class);

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SELLER', 'ADMIN')")
    public ResponseEntity<List<String>>
    getAllProducts(@NonNull @AuthenticationPrincipal UserDto userDto) {
        logger.info("authenticationPrincipal: {}", userDto.userId());
        List<String> products = Arrays.asList("Product1", "Product2");
        return ResponseEntity.ok(products);
    }
}
