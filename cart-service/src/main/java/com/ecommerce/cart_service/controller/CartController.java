package com.ecommerce.cart_service.controller;

import com.ecommerce.cart_service.dto.*;
import com.ecommerce.cart_service.service.CartService;
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
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private static final Logger logger = LogManager.getLogger(CartController.class);

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> getCart(@AuthenticationPrincipal UserDto userDto) {
        logger.info("Get cart for user: {}", userDto.userId());
        List<CartItemResponse> responses = cartService.getCart(userDto.userId());

        return ResponseEntity.ok(ApiResponse.success("Successfully found the cart.", responses));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCart(@AuthenticationPrincipal UserDto userDto) {
        logger.info("Delete cart for user: {}", userDto.userId());
        cartService.deleteCart(userDto.userId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartItemResponse>> createCartItem(@Valid @RequestBody CreateCartItemRequest request,
                                                 @AuthenticationPrincipal UserDto userDto) {
        logger.info("Create cart item for product {} for user {}", request.productId(), userDto.userId());
        CartItemResponse response = cartService.createCartItem(request, userDto.userId());

        return ResponseEntity.ok(ApiResponse.success("Successfully created cart item.", response));
    }

    @GetMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartItemResponse>> getCartItem(@PathVariable UUID cartItemId, @AuthenticationPrincipal UserDto userDto) {
        logger.info("Get cart item for cartId {} for user {}", cartItemId, userDto.userId());
        CartItemResponse response = cartService.getCartItem(cartItemId, userDto.userId());

        return ResponseEntity.ok(ApiResponse.success("Successfully found cart item.", response));
    }

    @PatchMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartItemResponse>> updateCartItem(@Valid @RequestBody UpdateCartItemRequest request, @PathVariable UUID cartItemId,
                                                 @AuthenticationPrincipal UserDto userDto) {
        logger.info("Update cart item {} for user {}", cartItemId , userDto.userId());
        CartItemResponse response = cartService.updateCartItem(request, cartItemId, userDto.userId());

        return ResponseEntity.ok(ApiResponse.success("Successfully Updated cart item.", response));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable UUID cartItemId, @AuthenticationPrincipal UserDto userDto) {
        logger.info("Delete cart item {} for user {}", cartItemId, userDto.userId());
        cartService.deleteCartItem(cartItemId, userDto.userId());

        return ResponseEntity.noContent().build();
    }
}
