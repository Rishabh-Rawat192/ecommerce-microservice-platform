package com.ecommerce.catalog_service.repository.specification;

import com.ecommerce.catalog_service.entity.CatalogProduct;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.beans.Expression;
import java.math.BigDecimal;
import java.util.UUID;

public class CatalogProductSpecifications {

    public static Specification<CatalogProduct> hasSellerId(UUID sellerId) {
        return (root, query, criteriaBuilder) ->
                sellerId == null ? null : criteriaBuilder.equal(root.get("sellerId"), sellerId);
    }

    public static Specification<CatalogProduct> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                                                    "%" + name.toLowerCase() + "%");
    }

    public static Specification<CatalogProduct> hasCategory(String category) {
        return (root, query, criteriaBuilder) ->
                category == null ? null : criteriaBuilder.equal(criteriaBuilder.lower(root.get("category")),
                                                                category.toLowerCase());
    }

    public static Specification<CatalogProduct> hasBrand(String brand) {
        return (root, query, criteriaBuilder) ->
                brand == null ? null : criteriaBuilder.equal(criteriaBuilder.lower(root.get("brand")),
                                                             brand.toLowerCase());
    }

    public static Specification<CatalogProduct> hasInStock(Boolean inStock) {
        return (root, query, criteriaBuilder) ->
                inStock == null ? null : criteriaBuilder.equal(root.get("inStock"), inStock);
    }

    public static Specification<CatalogProduct> hasPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null)
                return null;

            if (minPrice != null && maxPrice != null)
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);

            if (minPrice != null)
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);

            return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }
}
