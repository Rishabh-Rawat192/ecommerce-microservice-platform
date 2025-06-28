package com.ecommerce.inventory_service.controller;

import com.ecommerce.inventory_service.dto.*;
import com.ecommerce.inventory_service.service.StockService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal/v1/inventory")
@RequiredArgsConstructor
@Validated
public class InternalInventoryController {

    private static final Logger logger = LogManager.getLogger(InternalInventoryController.class);
    private final StockService stockService;

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<StockResponse>> getStock(@PathVariable @NotNull UUID productId) {
        logger.info("Getting product: {}", productId);

        StockResponse response = stockService.getStock(productId);
        return ResponseEntity.ok(ApiResponse.success("Product found successfully.", response));
    }

    @PostMapping("/reservations")
    public ResponseEntity<ApiResponse<List<ReserveStockItemResponse>>> createReservation(@Valid @RequestBody ReserveStockRequest request) {
        logger.info("Creating Reservation for order: {}", request.orderId());

        List<ReserveStockItemResponse> response = stockService.createReservation(request);
        return ResponseEntity.ok(ApiResponse.success("Stock reserved successfully.", response));
    }

    @PostMapping("/reservations/{orderId}/confirm")
    public ResponseEntity<ApiResponse<?>> confirmReservation(@PathVariable @NotNull UUID orderId) {
        logger.info("Confirming reservation for order: {}", orderId);

        stockService.confirmReservation(orderId);
        return ResponseEntity.ok(ApiResponse.success("Successfully confirmed reservation.", null));
    }
}
