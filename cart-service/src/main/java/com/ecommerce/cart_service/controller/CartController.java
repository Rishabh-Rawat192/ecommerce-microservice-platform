package com.ecommerce.cart_service.controller;

import com.ecommerce.cart_service.dto.UserDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private static final Logger logger = LogManager.getLogger(CartController.class);

    @GetMapping
    public ResponseEntity<String> getCart(@AuthenticationPrincipal UserDto userDto) {
        logger.info("Get cart for user: {}", userDto.userId());
        return ResponseEntity.ok("Get cart for user: " + userDto.userId());
    }
}
