package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.ProductFilterRequest;
import com.ecommerce.catalog_service.dto.ProductResponse;

import com.ecommerce.catalog_service.entity.CatalogProduct;
import com.ecommerce.catalog_service.repository.CatalogProductRepository;
import com.ecommerce.catalog_service.repository.specification.CatalogProductSpecifications;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.FetchNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CatalogProductServiceImp implements CatalogProductService {

    private static final Logger logger = LogManager.getLogger(CatalogProductServiceImp.class);

    private final CatalogProductRepository catalogProductRepository;

    @Override
    public Page<ProductResponse> getProducts(ProductFilterRequest request) {
        Specification<CatalogProduct> specification = Specification.where(CatalogProductSpecifications.hasName(request.name()))
                .and(CatalogProductSpecifications.hasCategory(request.category()));


        Pageable pageable = PageRequest.of(
                request.page() == null ? 0 : request.page(),
                request.size() == null ? 20 : request.size()
        );

        Page<CatalogProduct> productPage = catalogProductRepository.findAll(specification, pageable);

        logger.info("Fetched {} products from the database", productPage.getTotalElements());

        return productPage.map(ProductResponse::from);
    }

    @Override
    public ProductResponse getById(UUID productId) {
        // TODO: Need to define custom exception handling
        CatalogProduct product = catalogProductRepository.findById(productId)
                .orElseThrow();

        return ProductResponse.from(product);
    }
}
