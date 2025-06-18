package com.ecommerce.order_service.client;

import com.ecommerce.order_service.dto.ApiResponse;
import com.ecommerce.order_service.dto.CartItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "cart-service", url = "${cart.service.url}")
public interface CartClient {

    @GetMapping("/internal/v1/cart/{userId}")
    ApiResponse<List<CartItemResponse>> getCartItems(@PathVariable UUID userId);
}
