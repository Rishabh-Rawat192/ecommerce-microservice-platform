package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.dto.ProductCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductConsumerServiceImpl implements ProductConsumerService {

    private static final Logger logger = LogManager.getLogger(ProductConsumerServiceImpl.class);

    private final ObjectMapper objectMapper;
    private final Validator validator;
    private final InventoryService inventoryService;

    @KafkaListener(
            topics = "${kafka.topic.productCreated}",
            groupId = "${spring.kafka.consumer.group-id",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Override
    public void consumeProductCreatedEvent(String productCreatedEvent) {
        logger.info("Received Product Created Event: {}", productCreatedEvent);

        try {
            ProductCreatedEvent event = objectMapper.readValue(productCreatedEvent, ProductCreatedEvent.class);
            logger.info("Parsed Product Created Event: {}", event);

            Set<ConstraintViolation<ProductCreatedEvent>> violations = validator.validate(event);
            if (!violations.isEmpty()) {
                logger.error("Validation failed for ProductCreatedEvent: {}", violations);
                return;
            }

            inventoryService.createProduct(event);
        } catch (Exception e) {
            logger.error("Error processing Product Created Event: {}", e.getMessage());
        }

    }
}
