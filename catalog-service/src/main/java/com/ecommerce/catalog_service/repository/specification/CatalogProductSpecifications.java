package com.ecommerce.catalog_service.repository.specification;

import com.ecommerce.catalog_service.entity.CatalogProduct;
import org.springframework.data.jpa.domain.Specification;

public class CatalogProductSpecifications {
    public static Specification<CatalogProduct> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ? null : criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<CatalogProduct> hasCategory(String category) {
        return (root, query, criteriaBuilder) ->
                category == null ? null : criteriaBuilder.equal(root.get("category"), "%" + category + "%");
    }
}
