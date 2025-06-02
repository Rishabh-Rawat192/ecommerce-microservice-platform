package com.ecommerce.inventory_service.controller;

import com.ecommerce.inventory_service.dto.ApiResponse;
import com.ecommerce.inventory_service.dto.StockResponse;
import com.ecommerce.inventory_service.dto.StockUpdateRequest;
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
@RequestMapping("/internal/api/v1/inventory")
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
}
