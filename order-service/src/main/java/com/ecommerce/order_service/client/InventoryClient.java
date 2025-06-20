package com.ecommerce.order_service.client;

import com.ecommerce.order_service.dto.ApiResponse;
import com.ecommerce.order_service.dto.ReserveStockItemResponse;
import com.ecommerce.order_service.dto.ReserveStockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "inventory-service", url = "${inventory.service.url}")
public interface InventoryClient {

    @PostMapping("/internal/v1/inventory/reserve")
    ApiResponse<List<ReserveStockItemResponse>> reserveStocks(@RequestBody ReserveStockRequest request);
}
