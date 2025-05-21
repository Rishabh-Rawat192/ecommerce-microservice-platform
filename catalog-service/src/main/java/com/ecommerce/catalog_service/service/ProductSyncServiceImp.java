package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.ProductCreatedEvent;
import com.ecommerce.catalog_service.dto.ProductDeletedEvent;
import com.ecommerce.catalog_service.dto.ProductUpdatedEvent;
import com.ecommerce.catalog_service.entity.CatalogProduct;
import com.ecommerce.catalog_service.repository.CatalogProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductSyncServiceImp implements ProductSyncService {

    private final CatalogProductRepository catalogProductRepository;

    @Override
    public void createProduct(ProductCreatedEvent event) {
        if (catalogProductRepository.existsById(event.productId()))
            throw new IllegalArgumentException("Product already exists.");

        CatalogProduct product = CatalogProduct.builder()
                .id(event.productId())
                .sellerId(event.sellerId())
                .name(event.name())
                .description(event.description())
                .price(event.price())
                .category(event.category())
                .brand(event.brand())
                .imageUrl(event.imageUrl())
                .inStock(false)
                .build();

        catalogProductRepository.save(product);
    }

    @Override
    public void updateProduct(ProductUpdatedEvent event) {
        CatalogProduct product = catalogProductRepository.findById(event.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found."));

        if (event.name() != null) {
            product.setName(event.name());
        }
        if (event.description() != null) {
            product.setDescription(event.description());
        }
        if (event.price() != null) {
            product.setPrice(event.price());
        }
        if (event.category() != null) {
            product.setCategory(event.category());
        }
        if (event.brand() != null) {
            product.setBrand(event.brand());
        }
        if (event.imageUrl() != null) {
            product.setImageUrl(event.imageUrl());
        }

        catalogProductRepository.save(product);
    }

    @Override
    public void deleteProduct(ProductDeletedEvent event) {
        CatalogProduct product = catalogProductRepository.findById(event.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found."));

        catalogProductRepository.delete(product);
    }
}
