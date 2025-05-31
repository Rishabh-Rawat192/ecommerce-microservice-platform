package com.ecommerce.inventory_service.controller;

import com.ecommerce.inventory_service.dto.ApiResponse;
import com.ecommerce.inventory_service.dto.StockUpdateRequest;
import com.ecommerce.inventory_service.dto.StockUpdateResponse;
import com.ecommerce.inventory_service.dto.UserDto;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private static final Logger logger = LogManager.getLogger(InventoryController.class);

    @PostMapping("/{productId}/restock")
    public ResponseEntity<ApiResponse<StockUpdateResponse>> restock(@PathVariable UUID productId, @Valid @RequestBody StockUpdateRequest request,
                                                                    @AuthenticationPrincipal UserDto userDto) {
        logger.info("Restocking product: {}, Stock: {}, IdempotencyKey: {}", productId, request.stock(), request.idempotencyKey());
        logger.info("Authentication Principal in restock is: {}", userDto);
        StockUpdateResponse response = new StockUpdateResponse(productId, 20 + request.stock(), 10);
        return ResponseEntity.ok(ApiResponse.success("Restocked product: " + productId, response));
    }

    @PostMapping("/{productId}/deduct")
    public ResponseEntity<ApiResponse<StockUpdateResponse>> deduct(@PathVariable UUID productId, @Valid @RequestBody StockUpdateRequest request,
                                                                   @AuthenticationPrincipal UserDto userDto) {
        logger.info("Deducting product: {}, Stock: {}, IdempotencyKey: {}", productId, request.stock(), request.idempotencyKey());
        logger.info("Authentication Principal in deduct is: {}", userDto);
        StockUpdateResponse response = new StockUpdateResponse(productId, 20 + request.stock(), 10);
        return ResponseEntity.ok(ApiResponse.success("Deducted product: " + productId, response));
    }
}
