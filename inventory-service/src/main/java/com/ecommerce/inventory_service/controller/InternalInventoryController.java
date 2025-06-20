package com.ecommerce.inventory_service.controller;

import com.ecommerce.inventory_service.dto.*;
import com.ecommerce.inventory_service.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal/v1/inventory")
@RequiredArgsConstructor
public class InternalInventoryController {

    private static final Logger logger = LogManager.getLogger(InternalInventoryController.class);
    private final StockService stockService;

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<StockResponse>> getStock(@PathVariable UUID productId) {
        logger.info("Getting product: {}", productId);

        StockResponse response = stockService.getStock(productId);
        return ResponseEntity.ok(ApiResponse.success("Product found successfully.", response));
    }

    @PostMapping("/reserve")
    public ResponseEntity<ApiResponse<List<ReserveStockItemResponse>>> reserveStock(@Valid @RequestBody ReserveStockRequest request) {
        logger.info("Reserving stock for order: {}", request.orderId());

        List<ReserveStockItemResponse> response = stockService.reserveStock(request);
        return ResponseEntity.ok(ApiResponse.success("Stock reserved successfully.", response));
    }
}
