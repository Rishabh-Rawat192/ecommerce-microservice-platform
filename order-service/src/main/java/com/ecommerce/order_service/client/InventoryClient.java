package com.ecommerce.order_service.client;

import com.ecommerce.order_service.dto.ApiResponse;
import com.ecommerce.order_service.dto.ReserveStockItemResponse;
import com.ecommerce.order_service.dto.ReserveStockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "inventory-service", url = "${inventory.service.url}", path = "/internal/v1/inventory/reservations")
public interface InventoryClient {

    @PostMapping
    ApiResponse<List<ReserveStockItemResponse>> createReservation(@RequestBody ReserveStockRequest request);

    @PostMapping("/{orderId}/confirm")
    ApiResponse<?> confirmReservation(@PathVariable UUID orderId);

}
