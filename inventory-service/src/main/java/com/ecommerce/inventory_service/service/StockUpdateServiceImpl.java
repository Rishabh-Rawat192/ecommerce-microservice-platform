package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.dto.StockUpdateRequest;
import com.ecommerce.inventory_service.dto.StockUpdateResponse;
import com.ecommerce.inventory_service.entity.Inventory;
import com.ecommerce.inventory_service.exception.ApiException;
import com.ecommerce.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.requests.ApiError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockUpdateServiceImpl implements StockUpdateService {

    private static final Logger logger = LogManager.getLogger(StockUpdateServiceImpl.class);

    private final InventoryRepository inventoryRepository;

    @Override
    public StockUpdateResponse restock(StockUpdateRequest request, UUID productId, UUID sellerId) {
        Inventory inventory = getAuthorizedInventory(productId, sellerId);

        inventory.setTotalQuantity(inventory.getTotalQuantity() + request.stock());
        inventoryRepository.save(inventory);

        logger.info("Restocked productId={} by sellerId={} with quantity={}", productId, sellerId, request.stock());
        return StockUpdateResponse.from(inventory);
    }

    @Override
    public StockUpdateResponse deductStock(StockUpdateRequest request, UUID productId, UUID sellerId) {
        Inventory inventory = getAuthorizedInventory(productId, sellerId);

        int availableStocks = inventory.getTotalQuantity() - inventory.getReservedQuantity();
        if (availableStocks < request.stock())
            throw new ApiException("Not enough available stock to deduct.", HttpStatus.BAD_REQUEST);

        inventory.setTotalQuantity(inventory.getTotalQuantity() - request.stock());
        inventoryRepository.save(inventory);

        logger.info("Deducted stock from productId={} by sellerId={} quantity={}", productId, sellerId, request.stock());
        return StockUpdateResponse.from(inventory);
    }

    private Inventory getAuthorizedInventory(UUID productId, UUID sellerId) {
        Inventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new ApiException("Inventory not found.", HttpStatus.NOT_FOUND));

        if (!inventory.getSellerId().equals(sellerId))
            throw new ApiException("Unauthorized Access.", HttpStatus.UNAUTHORIZED);

        return inventory;
    }
}
