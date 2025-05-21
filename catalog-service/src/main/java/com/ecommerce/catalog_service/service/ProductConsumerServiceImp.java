package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.ProductCreatedEvent;
import com.ecommerce.catalog_service.dto.ProductDeletedEvent;
import com.ecommerce.catalog_service.dto.ProductUpdatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.*;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductConsumerServiceImp implements ProductConsumerService{

    private static final Logger logger = LogManager.getLogger(ProductConsumerServiceImp.class);

    private final ProductSyncService productSyncService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @KafkaListener(
            topics = "${kafka.topic.productCreated}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Override
    public void consumeProductCreatedEvent(String productCreatedEvent) {
        logger.info("Received Product Created Event: {}", productCreatedEvent);

        try {
            ProductCreatedEvent event = objectMapper.readValue(productCreatedEvent, ProductCreatedEvent.class);
            logger.info("Parsed Product Created Event: {}", event);

            // Validate the event
            Set<ConstraintViolation<ProductCreatedEvent>> violations = validator.validate(event);
            if (!violations.isEmpty()) {
                logger.error("Validation failed for ProductCreatedEvent: {}", violations);
                return;
            }

            productSyncService.createProduct(event);
        } catch (Exception e) {
            logger.error("Error processing Product Created Event: {}", e.getMessage());
        }
    }

    @KafkaListener(
            topics = "${kafka.topic.productUpdated}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Override
    public void consumeProductUpdatedEvent(String productUpdatedEvent) {
        logger.info("Received Product Updated Event: {}", productUpdatedEvent);

        try {
            ProductUpdatedEvent event = objectMapper.readValue(productUpdatedEvent, ProductUpdatedEvent.class);
            logger.info("Parsed Product Updated Event: {}", event);

            // Validate the event
            Set <ConstraintViolation<ProductUpdatedEvent>> violations = validator.validate(event);
            if (!violations.isEmpty()) {
                logger.error("Validation failed for ProductUpdatedEvent: {}", violations);
            }

            productSyncService.updateProduct(event);
        } catch (Exception e) {
            logger.error("Error processing Product Updated Event: {}", e.getMessage());
        }
    }

    @KafkaListener(
            topics = "${kafka.topic.productDeleted}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Override
    public void consumeProductDeletedEvent(String productDeletedEvent) {
        logger.info("Received Product Deleted Event: {}", productDeletedEvent);

        try {
            ProductDeletedEvent event = objectMapper.readValue(productDeletedEvent, ProductDeletedEvent.class);
            logger.info("Parsed Product Deleted Event: {}", event);

            // Validate the event
            Set <ConstraintViolation<ProductDeletedEvent>> violations = validator.validate(event);
            if (!violations.isEmpty()) {
                logger.error("Validation failed for ProductDeletedEvent: {}", violations);
            }

            productSyncService.deleteProduct(event);
        } catch (Exception e) {
            logger.error("Error processing Product Deleted Event: {}", e.getMessage());
        }
    }
}
