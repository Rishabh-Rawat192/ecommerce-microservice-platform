package com.ecommerce.order_service.controller;

import com.ecommerce.order_service.dto.*;
import com.ecommerce.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private static final Logger logger = LogManager.getLogger(OrderController.class);
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderCreateResponse>> createOrder(@AuthenticationPrincipal UserDto userDto) {
        logger.debug("Received request to create order for user: {}.", userDto.email());

        OrderCreateResponse response = orderService.createOrder(userDto.userId());
        return ResponseEntity.ok(ApiResponse.success("Order draft created successfully.", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders(@AuthenticationPrincipal UserDto userDto) {
        logger.debug("Received request to get all orders for user: {}.", userDto.email());

        List<OrderResponse> response = orderService.getAllOrders(userDto.userId());
        return ResponseEntity.ok(ApiResponse.success("Orders found successfully.", response));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable UUID orderId, @AuthenticationPrincipal UserDto userDto) {
        logger.debug("Received request to find order: {} for user: {}.", orderId, userDto.email());

        OrderResponse response = orderService.getOrder(orderId, userDto.userId());
        return ResponseEntity.ok(ApiResponse.success("Order found successfully.", response));
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<ApiResponse<OrderResponse>> confirmOrder(@PathVariable UUID orderId, @AuthenticationPrincipal UserDto userDto) {
        logger.debug("Received request to confirm order: {} for user: {}.", orderId, userDto.email());

        OrderResponse response = orderService.confirmOrder(orderId, userDto.userId());
        return ResponseEntity.ok(ApiResponse.success("Order confirmed successfully.", response));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(@PathVariable UUID orderId, @AuthenticationPrincipal UserDto userDto) {
        logger.debug("Received request to cancel order: {} for user: {}.", orderId, userDto.email());

        OrderResponse response = orderService.cancelOrder(orderId, userDto.userId());
        return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully.", response));
    }
}
