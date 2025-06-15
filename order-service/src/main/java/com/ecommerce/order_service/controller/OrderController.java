package com.ecommerce.order_service.controller;

import com.ecommerce.order_service.dto.ApiResponse;
import com.ecommerce.order_service.dto.OrderItemResponse;
import com.ecommerce.order_service.dto.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private static Logger logger = LogManager.getLogger(OrderController.class);

    @PostMapping
    public ResponseEntity<ApiResponse<OrderItemResponse>> createOrder(@AuthenticationPrincipal UserDto userDto) {
        logger.debug("Received request to create order for user: {}.", userDto.email());
        return ResponseEntity.ok(ApiResponse.success("Order draft created successfully.", null));
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<ApiResponse<OrderItemResponse>> confirmOrder(@PathVariable UUID orderId, @AuthenticationPrincipal UserDto userDto) {
        logger.debug("Received request to confirm order: {} for user: {}.", orderId, userDto.email());
        return ResponseEntity.ok(ApiResponse.success("Order confirmed successfully.", null));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable UUID orderId, @AuthenticationPrincipal UserDto userDto) {
        logger.debug("Received request to cancel order: {} for user: {}.", orderId, userDto.email());
        return ResponseEntity.notFound().build();
    }
}
