package com.ecommerce.product_service.controller;

import com.ecommerce.product_service.dto.*;
import com.ecommerce.product_service.service.ProductService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private static Logger logger = LogManager.getLogger(ProductController.class);
    private final ProductService productService;

    @GetMapping("/mine")
    @PreAuthorize("hasAnyAuthority('SELLER')")
    public ResponseEntity<ApiResponse<List<ProductResponse>>>
    getAllProductsBySellerId(@NonNull @AuthenticationPrincipal UserDto userDto) {
        List<ProductResponse> response = productService.getAllBySellerId(userDto.userId());
        return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully.", response));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('SELLER')")
    public ResponseEntity<ApiResponse<ProductResponse>>
    registerProduct(@Valid @RequestBody ProductRegisterRequest request, @NonNull @AuthenticationPrincipal UserDto userDto) {
        ProductResponse response = productService.register(request, userDto.userId());
        return ResponseEntity.ok(ApiResponse.success("Product registered successfully.", response));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>>
    getProduct(@PathVariable UUID productId) {
        ProductResponse response = productService.get(productId);
        return ResponseEntity.ok(ApiResponse.success("Product retrieved successfully.", response));
    }

    @PatchMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('SELLER')")
    public ResponseEntity<ApiResponse<ProductResponse>>
    updateProduct(@Valid @RequestBody ProductUpdateRequest request, @PathVariable UUID productId,
                  @NonNull @AuthenticationPrincipal UserDto userDto) {
        ProductResponse response = productService.update(request, productId, userDto.userId());
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully.", response));
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyAuthority('SELLER')")
    public ResponseEntity<Void>
    deleteProduct(@PathVariable UUID productId,
                  @NonNull @AuthenticationPrincipal UserDto userDto) {
        productService.delete(productId, userDto.userId());
        return ResponseEntity.noContent().build();
    }
}
