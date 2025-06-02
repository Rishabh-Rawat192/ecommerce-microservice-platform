package com.ecommerce.inventory_service.controller;

import com.ecommerce.inventory_service.dto.ApiResponse;
import com.ecommerce.inventory_service.dto.StockUpdateRequest;
import com.ecommerce.inventory_service.dto.StockResponse;
import com.ecommerce.inventory_service.dto.UserDto;
import com.ecommerce.inventory_service.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private static final Logger logger = LogManager.getLogger(InventoryController.class);

    private final StockService stockService;

    @PostMapping("/{productId}/restock")
    public ResponseEntity<ApiResponse<StockResponse>> restock(@PathVariable UUID productId, @Valid @RequestBody StockUpdateRequest request,
                                                              @AuthenticationPrincipal UserDto userDto) {
        logger.info("Restocking product: {}, Stock: {}", productId, request.stock());
        logger.info("Authentication Principal in restock is: {}", userDto);

        StockResponse response = stockService.restock(request, productId, userDto.userId());
        return ResponseEntity.ok(ApiResponse.success("Restocked product: " + productId, response));
    }

    @PostMapping("/{productId}/deduct")
    public ResponseEntity<ApiResponse<StockResponse>> deductStock(@PathVariable UUID productId, @Valid @RequestBody StockUpdateRequest request,
                                                                  @AuthenticationPrincipal UserDto userDto) {
        logger.info("Deducting product: {}, Stock: {}", productId, request.stock());
        logger.info("Authentication Principal in deduct is: {}", userDto);

        StockResponse response = stockService.deductStock(request, productId, userDto.userId());
        return ResponseEntity.ok(ApiResponse.success("Deducted product: " + productId, response));
    }
}
