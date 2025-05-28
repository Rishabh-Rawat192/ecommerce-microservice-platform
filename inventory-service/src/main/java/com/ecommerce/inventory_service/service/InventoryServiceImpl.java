package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.dto.ProductCreatedEvent;
import com.ecommerce.inventory_service.entity.Inventory;
import com.ecommerce.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private static final Logger logger = LogManager.getLogger(InventoryServiceImpl.class);

    private final InventoryRepository inventoryRepository;

    @Override
    public void createProduct(ProductCreatedEvent event) {
        if (inventoryRepository.existsById(event.productId())) {
            logger.warn("Attempt to create a product that already exists: {}", event.productId());
            throw new IllegalArgumentException("Product already exists.");
        }

        Inventory inventory = Inventory.builder()
                .productId(event.productId())
                .sellerId(event.sellerId())
                .reservedQuantity(0)
                .totalQuantity(0)
                .build();

        inventoryRepository.save(inventory);
        logger.info("Product created successfully: {}", event.productId());
    }
}
