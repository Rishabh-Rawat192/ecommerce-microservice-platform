package com.ecommerce.cart_service.controller;

import com.ecommerce.cart_service.dto.CreateCartItemRequest;
import com.ecommerce.cart_service.dto.UpdateCartItemRequest;
import com.ecommerce.cart_service.dto.UserDto;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private static final Logger logger = LogManager.getLogger(CartController.class);

    @GetMapping
    public ResponseEntity<String> getCart(@AuthenticationPrincipal UserDto userDto) {
        logger.info("Get cart for user: {}", userDto.userId());
        return ResponseEntity.ok("Found cart for user: " + userDto.userId());
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCart(@AuthenticationPrincipal UserDto userDto) {
        logger.info("Delete cart for user: {}", userDto.userId());
        return ResponseEntity.ok("Deleted cart for user: " + userDto.userId());
    }

    @PostMapping("/items")
    public ResponseEntity<String> createCartItem(@Valid @RequestBody CreateCartItemRequest request,
                                                 @AuthenticationPrincipal UserDto userDto) {
        logger.info("Create cart item for product {} for user {}",request.productId(), userDto.userId());
        return ResponseEntity.ok("Created item: " + request.productId());
    }

    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<String> updateCartItem(@Valid @RequestBody UpdateCartItemRequest request, @PathVariable UUID cartItemId,
                                                 @AuthenticationPrincipal UserDto userDto) {
        logger.info("Update cart item {} for user {}", cartItemId , userDto.userId());
        return ResponseEntity.ok("Updated item: " + cartItemId);
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<String> deleteCartItem(@PathVariable UUID cartItemId, @AuthenticationPrincipal UserDto userDto) {
        logger.info("Delete cart item {} for user {}", cartItemId, userDto.userId());
        return ResponseEntity.ok("Deleted item: " + cartItemId);
    }
}
