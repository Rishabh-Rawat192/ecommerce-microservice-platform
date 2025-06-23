package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.dto.*;
import com.ecommerce.inventory_service.entity.Inventory;
import com.ecommerce.inventory_service.entity.Reservation;
import com.ecommerce.inventory_service.entity.ReservationItem;
import com.ecommerce.inventory_service.enums.ReservationItemStatus;
import com.ecommerce.inventory_service.enums.ReservationStatus;
import com.ecommerce.inventory_service.exception.ApiException;
import com.ecommerce.inventory_service.repository.InventoryRepository;
import com.ecommerce.inventory_service.repository.ReservationRepository;
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
    private final ReservationRepository reservationRepository;
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
    public List<ReserveStockItemResponse> createReservation(ReserveStockRequest request) {
        if (reservationRepository.existsById(request.orderId()))
            throw new ApiException("Duplicate reservation request.", HttpStatus.BAD_REQUEST);

        Set<UUID> productIds = validateAndGetProductIds(request);
        List<Inventory> availableProducts = inventoryRepository.findAllById(productIds);
        Map<UUID, Inventory> productIdToInventory = availableProducts.stream()
                .collect(Collectors.toMap(Inventory::getProductId, Function.identity()));

        Reservation reservation = Reservation.builder()
                .orderId(request.orderId())
                .reservationStatus(ReservationStatus.RESERVED)
                .build();

        List<ReserveStockItemResponse> responses = new ArrayList<>();
        List<Inventory> inventoriesToUpdate = new ArrayList<>();

        for (ReserveStockItemRequest item : request.items()) {
            UUID productId = item.productId();
            int requestedQuantity = item.quantity();

            Inventory inventory = productIdToInventory.get(productId);
            if (inventory == null) {
                responses.add(new ReserveStockItemResponse(productId, requestedQuantity, 0, ReservationItemStatus.NOT_FOUND));
                continue;
            }

            int reservedQuantity = Math.min(requestedQuantity, inventory.getAvailableStocks());
            if (reservedQuantity == 0) {
                responses.add(new ReserveStockItemResponse(productId, requestedQuantity, 0, ReservationItemStatus.OUT_OF_STOCK));
                continue;
            }

            inventory.setReservedQuantity(inventory.getReservedQuantity() + reservedQuantity);
            inventoriesToUpdate.add(inventory);

            ReservationItem reservationItem = ReservationItem.builder()
                    .productId(productId)
                    .reservedQuantity(reservedQuantity)
                    .build();
            reservation.addItem(reservationItem);

            responses.add(new ReserveStockItemResponse(productId, requestedQuantity, reservedQuantity, ReservationItemStatus.RESERVED));
        }

        inventoryRepository.saveAll(inventoriesToUpdate);
        reservationRepository.save(reservation);
        return responses;
    }

    @Transactional
    @Override
    public void confirmReservation(UUID orderId) {
        Reservation reservation = reservationRepository.findById(orderId)
                .orElseThrow(() -> new ApiException("No reservation found.", HttpStatus.BAD_REQUEST));

        if (!reservation.getReservationStatus().equals(ReservationStatus.RESERVED))
            throw new ApiException("Can't confirm order.", HttpStatus.BAD_REQUEST);

        deductInventoryStock(reservation);
        reservation.setReservationStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);
    }

    @Transactional
    @Override
    public void cancelReservation(UUID orderId) {
        Reservation reservation = reservationRepository.findById(orderId)
                .orElseThrow(() -> new ApiException("No reservation found.", HttpStatus.BAD_REQUEST));

        if (reservation.getReservationStatus().equals(ReservationStatus.CANCELLED))
            throw new ApiException("Already cancelled order.", HttpStatus.BAD_REQUEST);

        if (reservation.getReservationStatus().equals(ReservationStatus.CONFIRMED)) {
            restockInventory(reservation);
        } else if(reservation.getReservationStatus().equals(ReservationStatus.RESERVED)) {
            releaseInventoryReservation(reservation);
        }

        reservation.setReservationStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    private void deductInventoryStock(Reservation reservation) {
        List<Inventory> inventoryToUpdate = new ArrayList<>();

        for (ReservationItem item : reservation.getItems()) {
            UUID productId = item.getProductId();
            int reservedQuantity = item.getReservedQuantity();

            Inventory inventory = inventoryRepository.findById(productId)
                    .orElseThrow(() -> new ApiException("Product inventory not found for product: " + productId,
                            HttpStatus.NOT_FOUND));

            if (inventory.getReservedQuantity() < reservedQuantity) {
                throw new ApiException("Invalid stock reservation value of product: " + productId, HttpStatus.BAD_REQUEST);
            }

            inventory.setTotalQuantity(inventory.getTotalQuantity() - reservedQuantity);
            inventory.setReservedQuantity(inventory.getReservedQuantity() - reservedQuantity);

            // Sanity check: reserved + available should always equal total
            if (inventory.getAvailableStocks() + inventory.getReservedQuantity() != inventory.getTotalQuantity()) {
                logger.error("Inventory state corrupted for product: {}", productId);
                throw new ApiException("Inventory state corrupted for product: " + productId, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            inventoryToUpdate.add(inventory);
        }

        inventoryRepository.saveAll(inventoryToUpdate);
    }

    private void restockInventory(Reservation reservation) {
        List<Inventory> inventoryToUpdate = new ArrayList<>();

        for (ReservationItem item : reservation.getItems()) {
            UUID productId = item.getProductId();
            int reservedQuantity = item.getReservedQuantity();

            Inventory inventory = inventoryRepository.findById(productId)
                    .orElseThrow(() -> new ApiException("Product inventory not found for product: " + productId,
                            HttpStatus.NOT_FOUND));

            inventory.setTotalQuantity(inventory.getTotalQuantity() + reservedQuantity);

            // Sanity check: reserved + available should always equal total
            if (inventory.getAvailableStocks() + inventory.getReservedQuantity() != inventory.getTotalQuantity()) {
                logger.error("Inventory state corrupted for product: {}", productId);
                throw new ApiException("Inventory state corrupted for product: " + productId, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            inventoryToUpdate.add(inventory);
        }

        inventoryRepository.saveAll(inventoryToUpdate);
    }

    private void releaseInventoryReservation(Reservation reservation) {
        List<Inventory> inventoryToUpdate = new ArrayList<>();

        for (ReservationItem item : reservation.getItems()) {
            UUID productId = item.getProductId();
            int reservedQuantity = item.getReservedQuantity();

            Inventory inventory = inventoryRepository.findById(productId)
                    .orElseThrow(() -> new ApiException("Product inventory not found for product: " + productId,
                            HttpStatus.NOT_FOUND));

            if (inventory.getReservedQuantity() < reservedQuantity) {
                throw new ApiException("Invalid reservation of product: " + productId, HttpStatus.BAD_REQUEST);
            }

            inventory.setReservedQuantity(inventory.getReservedQuantity() - reservedQuantity);

            // Sanity check: reserved + available should always equal total
            if (inventory.getAvailableStocks() + inventory.getReservedQuantity() != inventory.getTotalQuantity()) {
                logger.error("Inventory state corrupted for product: {}", productId);
                throw new ApiException("Inventory state corrupted for product: " + productId, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            inventoryToUpdate.add(inventory);
        }

        inventoryRepository.saveAll(inventoryToUpdate);
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
