package com.ecommerce.product_service.controller;

import com.ecommerce.product_service.dto.ApiResponse;
import com.ecommerce.product_service.dto.ProductRegisterRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.dto.UserDto;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.apache.coyote.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private static Logger logger = LogManager.getLogger(ProductController.class);

    @GetMapping
    @PreAuthorize("hasAnyAuthority('SELLER')")
    public ResponseEntity<List<String>>
    getAllProducts(@NonNull @AuthenticationPrincipal UserDto userDto) {
        logger.info("authenticationPrincipal: {}", userDto.userId());
        List<String> products = Arrays.asList("Product1", "Product2");
        return ResponseEntity.ok(products);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SELLER')")
    public ResponseEntity<ApiResponse<ProductResponse>>
    registerProduct(@Valid @RequestBody ProductRegisterRequest request, @NonNull @AuthenticationPrincipal UserDto userDto) {
        return ResponseEntity.ok(ApiResponse.success("Product registered", null));
    }

    @GetMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('SELLER')")
    public ResponseEntity<ApiResponse<ProductResponse>>
    getProduct(@PathVariable String productId, @NonNull @AuthenticationPrincipal UserDto userDto) {
        return ResponseEntity.ok(ApiResponse.success("Product fetched", null));
    }

    @PatchMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('SELLER')")
    public ResponseEntity<ApiResponse<ProductResponse>>
    updateProduct(@Valid @RequestBody ProductRegisterRequest request, @PathVariable String productId,
                  @NonNull @AuthenticationPrincipal UserDto userDto) {
        return ResponseEntity.ok(ApiResponse.success("Product updated", null));
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('SELLER')")
    public ResponseEntity<ApiResponse<ProductResponse>>
    deleteProduct(@PathVariable String productId,
                  @NonNull @AuthenticationPrincipal UserDto userDto) {
        return ResponseEntity.ok(ApiResponse.success("Product deleted", null));
    }
}
