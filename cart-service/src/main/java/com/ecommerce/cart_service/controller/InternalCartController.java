package com.ecommerce.cart_service.controller;

import com.ecommerce.cart_service.dto.ApiResponse;
import com.ecommerce.cart_service.dto.CartItemResponse;
import com.ecommerce.cart_service.dto.UserDto;
import com.ecommerce.cart_service.entity.CartItem;
import com.ecommerce.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/internal/v1/cart")
@RequiredArgsConstructor
public class InternalCartController {

    private static final Logger logger = LogManager.getLogger(InternalCartController.class);
    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> getCart(@PathVariable UUID userId) {
        logger.info("Internal Get cart for user: {}", userId);
        List<CartItemResponse> responses = cartService.getCart(userId);

        return ResponseEntity.ok(ApiResponse.success("Successfully found the cart.", responses));
    }
}
