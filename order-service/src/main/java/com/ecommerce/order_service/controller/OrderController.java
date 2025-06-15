package com.ecommerce.order_service.controller;

import com.ecommerce.order_service.dto.ApiResponse;
import com.ecommerce.order_service.dto.OrderItemResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private static Logger logger = LogManager.getLogger(OrderController.class);

    @PostMapping
    public ResponseEntity<ApiResponse<OrderItemResponse>> createOrder() {
        logger.info("Received request to create order.");
        return ResponseEntity.ok(ApiResponse.success("Order draft created successfully.", null));
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<ApiResponse<OrderItemResponse>> confirmOrder(@PathVariable UUID orderId) {
        logger.info("Received request to confirm order: {}.", orderId);
        return ResponseEntity.ok(ApiResponse.success("Order confirmed successfully.", null));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable UUID orderId) {
        logger.info("Received request to cancel order: {}.", orderId);
        return ResponseEntity.notFound().build();
    }
}
