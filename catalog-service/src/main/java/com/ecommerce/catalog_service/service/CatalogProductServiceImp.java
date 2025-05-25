package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.ApiResponse;
import com.ecommerce.catalog_service.dto.PagedResponse;
import com.ecommerce.catalog_service.dto.ProductFilterRequest;
import com.ecommerce.catalog_service.dto.ProductResponse;

import com.ecommerce.catalog_service.entity.CatalogProduct;
import com.ecommerce.catalog_service.exception.ApiException;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CatalogProductServiceImp implements CatalogProductService {

    private static final Logger logger = LogManager.getLogger(CatalogProductServiceImp.class);
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("name", "price", "category", "brand", "createdAt");
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final CatalogProductRepository catalogProductRepository;

    @Override
    public PagedResponse<ProductResponse> getProducts(ProductFilterRequest request) {
        Specification<CatalogProduct> specification = Specification.where(CatalogProductSpecifications.hasSellerId(request.sellerId()))
                .and(CatalogProductSpecifications.hasName(request.name()))
                .and(CatalogProductSpecifications.hasCategory(request.category()))
                .and(CatalogProductSpecifications.hasBrand(request.brand()))
                .and(CatalogProductSpecifications.hasInStock(request.inStock()))
                .and(CatalogProductSpecifications.hasPriceBetween(request.minPrice(), request.maxPrice()));

        Sort sort = Sort.unsorted();
        if (validSortField(request.sortBy())) {
            String direction = request.sortDirection() != null &&
                               (request.sortDirection().compareToIgnoreCase("asc") == 0 ||
                               request.sortDirection().compareToIgnoreCase("desc") == 0) ?
                               request.sortDirection() : "ASC";
            sort = Sort.by(Sort.Direction.fromString(direction), request.sortBy());
        }

        Pageable pageable = PageRequest.of(
                request.page() == null ? DEFAULT_PAGE : request.page(),
                request.size() == null ? DEFAULT_PAGE_SIZE : request.size(),
                sort
        );

        Page<CatalogProduct> productPage = catalogProductRepository.findAll(specification, pageable);

        logger.info("Fetched {} products from the database", productPage.getTotalElements());

        return PagedResponse.from(productPage.map(ProductResponse::from));
    }

    @Override
    public ProductResponse getById(UUID productId) {
        CatalogProduct product = catalogProductRepository.findById(productId)
                .orElseThrow(() -> new ApiException("Product not found.", HttpStatus.NOT_FOUND));

        return ProductResponse.from(product);
    }

    private boolean validSortField(String sortBy) {
        return sortBy != null && ALLOWED_SORT_FIELDS.contains(sortBy);
    }
}
