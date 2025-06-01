package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.StockStatusUpdatedEvent;
import com.ecommerce.catalog_service.entity.CatalogProduct;
import com.ecommerce.catalog_service.repository.CatalogProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class StockSyncServiceImpl implements StockSyncService {

    private static final Logger logger = LogManager.getLogger(StockSyncServiceImpl.class);

    private final CatalogProductRepository catalogProductRepository;

    @Override
    public void updateStockStatus(StockStatusUpdatedEvent event) {
        CatalogProduct product = catalogProductRepository.findById(event.productId())
                .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + event.productId()));

        product.setInStock(event.inStock());
        catalogProductRepository.save(product);

        logger.info("Stock is updated to {} for product with id: {}", event.inStock(), event.productId());
    }
}
