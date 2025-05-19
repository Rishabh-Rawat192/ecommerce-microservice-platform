package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.ProductCreatedEvent;
import com.ecommerce.product_service.dto.ProductRegisterRequest;
import com.ecommerce.product_service.dto.ProductResponse;
import com.ecommerce.product_service.dto.ProductUpdateRequest;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.exception.ApiException;
import com.ecommerce.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService{

    private final ProductRepository productRepository;
    private final ProductProducerService productProducerService;

    @Override
    public ProductResponse register(ProductRegisterRequest request, UUID sellerId) {
        Product product = Product.builder()
                .sellerId(sellerId)
                .name(request.name().trim())
                .description(request.description().trim())
                .category(request.category().trim())
                .brand(request.brand().trim())
                .price(request.price())
                .imageUrl(request.imageUrl().trim())
                .isActive(true)
                .build();

        productRepository.save(product);

        ProductCreatedEvent createdEvent = ProductCreatedEvent.from(product);

        productProducerService.sendProductCreationEvent(product.getId(), createdEvent);

        return ProductResponse.from(product);
    }

    @Override
    public ProductResponse get(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ApiException("Product not found.", HttpStatus.NOT_FOUND));

        return ProductResponse.from(product);
    }

    @Override
    public ProductResponse update(ProductUpdateRequest request, UUID productId, UUID sellerId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ApiException("Product not found.", HttpStatus.NOT_FOUND));

        if (!product.getSellerId().equals(sellerId))
            throw new ApiException("You are not authorized to update this product.", HttpStatus.UNAUTHORIZED);

        if (StringUtils.hasText(request.name()))
            product.setName(request.name().trim());
        if (StringUtils.hasText(request.description()))
            product.setDescription(request.description().trim());
        if (StringUtils.hasText(request.category()))
            product.setCategory(request.category().trim());
        if (StringUtils.hasText(request.brand()))
            product.setBrand(request.brand().trim());
        if (request.price() != null)
            product.setPrice(request.price());
        if (StringUtils.hasText(request.imageUrl()))
            product.setImageUrl(request.imageUrl().trim());

        productRepository.save(product);
        return ProductResponse.from(product);
    }

    @Override
    public void delete(UUID productId, UUID sellerId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new ApiException("Product not found.", HttpStatus.NOT_FOUND));

        if (!product.getSellerId().equals(sellerId))
            throw new ApiException("You are not authorized to delete this product.", HttpStatus.UNAUTHORIZED);

        productRepository.delete(product);
    }

    @Override
    public List<ProductResponse> getAllBySellerId(UUID sellerId) {
        List<Product> products = productRepository.findAllBySellerId(sellerId);

        return products.stream().map(ProductResponse::from).toList();
    }
}
