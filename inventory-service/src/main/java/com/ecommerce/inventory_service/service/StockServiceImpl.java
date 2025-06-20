package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.dto.*;
import com.ecommerce.inventory_service.entity.Inventory;
import com.ecommerce.inventory_service.entity.ReservationItem;
import com.ecommerce.inventory_service.entity.ReservationItemId;
import com.ecommerce.inventory_service.enums.ReservationItemStatus;
import com.ecommerce.inventory_service.exception.ApiException;
import com.ecommerce.inventory_service.repository.InventoryRepository;
import com.ecommerce.inventory_service.repository.ReservationItemRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private static final Logger logger = LogManager.getLogger(StockServiceImpl.class);

    private final InventoryRepository inventoryRepository;
    private final InventoryProducerService inventoryProducerService;
    private final ReservationItemRepository reservationItemRepository;
    private final EntityManager entityManager;

    @Override
    public StockResponse restock(StockUpdateRequest request, UUID productId, UUID sellerId) {
        Inventory inventory = getAuthorizedInventory(productId, sellerId);

        boolean wasOutOfStock = inventory.getTotalQuantity() == 0;

        inventory.setTotalQuantity(inventory.getTotalQuantity() + request.stock());
        inventoryRepository.save(inventory);

        if (wasOutOfStock)
            inventoryProducerService.sendStockStatusUpdatedEvent(new StockStatusUpdatedEvent(productId, true));

        logger.info("Restocked productId={} by sellerId={} with quantity={}", productId, sellerId, request.stock());
        return StockResponse.from(inventory);
    }

    @Override
    public StockResponse deductStock(StockUpdateRequest request, UUID productId, UUID sellerId) {
        Inventory inventory = getAuthorizedInventory(productId, sellerId);

        int availableStocks = inventory.getTotalQuantity() - inventory.getReservedQuantity();
        if (availableStocks < request.stock())
            throw new ApiException("Not enough available stock to deduct.", HttpStatus.BAD_REQUEST);

        int updatedTotalQuantity = inventory.getTotalQuantity() - request.stock();
        inventory.setTotalQuantity(updatedTotalQuantity);
        inventoryRepository.save(inventory);

        if (updatedTotalQuantity == 0)
            inventoryProducerService.sendStockStatusUpdatedEvent(new StockStatusUpdatedEvent(productId, false));

        logger.info("Deducted stock from productId={} by sellerId={} quantity={}", productId, sellerId, request.stock());
        return StockResponse.from(inventory);
    }

    @Override
    public StockResponse getStock(UUID productId) {
        Inventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new ApiException("Inventory not found.", HttpStatus.NOT_FOUND));

        return StockResponse.from(inventory);
    }

    @Transactional
    @Override
    public List<ReserveStockItemResponse> reserveStock(ReserveStockRequest request) {
        if (reservationItemRepository.existsByIdOrderId(request.orderId()))
            throw new ApiException("Duplicate reservation request.", HttpStatus.BAD_REQUEST);

        Set<UUID> productIds = validateAndGetProductIds(request);
        List<Inventory> availableProducts = inventoryRepository.findAllById(productIds);
        Map<UUID, Inventory> productIdToInventory = availableProducts.stream()
                .collect(Collectors.toMap(Inventory::getProductId, Function.identity()));

        List<ReserveStockItemResponse> responses = new ArrayList<>();
        List<Inventory> inventoriesToUpdate = new ArrayList<>();
        List<ReservationItem> reservationItemsToUpdate = new ArrayList<>();

        for (ReserveStockItemRequest item : request.items()) {
            UUID productId = item.productId();
            int requestedQuantity = item.quantity();

            Inventory inventory = productIdToInventory.get(productId);
            if (inventory == null) {
                responses.add(new ReserveStockItemResponse(productId, requestedQuantity, 0, ReservationItemStatus.NOT_FOUND));
                continue;
            }

            int availableStocks = inventory.getAvailableStocks();
            if (availableStocks <= 0) {
                responses.add(new ReserveStockItemResponse(productId, requestedQuantity, 0, ReservationItemStatus.OUT_OF_STOCK));
                continue;
            }

            int reservedQuantity = Math.min(requestedQuantity, availableStocks);
            inventory.setReservedQuantity(inventory.getReservedQuantity() + reservedQuantity);
            inventoriesToUpdate.add(inventory);

            ReservationItem reservationItem = ReservationItem.builder()
                    .id(new ReservationItemId(request.orderId(), productId))
                    .reservedQuantity(reservedQuantity)
                    .build();
            reservationItemsToUpdate.add(reservationItem);

            responses.add(new ReserveStockItemResponse(productId, requestedQuantity, reservedQuantity, ReservationItemStatus.RESERVED));
        }

        inventoryRepository.saveAll(inventoriesToUpdate);
        reservationItemRepository.saveAll(reservationItemsToUpdate);
        entityManager.flush();
        return responses;
    }

    private Set<UUID> validateAndGetProductIds(ReserveStockRequest request) {
        Set<UUID> productIds = new HashSet<>();
        for (ReserveStockItemRequest item : request.items()) {
            if (productIds.contains(item.productId()))
                throw new ApiException("Duplicate product in same request.", HttpStatus.BAD_REQUEST);
            productIds.add(item.productId());
        }

        return productIds;
    }

    private Inventory getAuthorizedInventory(UUID productId, UUID sellerId) {
        Inventory inventory = inventoryRepository.findById(productId)
                .orElseThrow(() -> new ApiException("Inventory not found.", HttpStatus.NOT_FOUND));

        if (!inventory.getSellerId().equals(sellerId))
            throw new ApiException("Unauthorized Access.", HttpStatus.UNAUTHORIZED);

        return inventory;
    }
}
