package com.ecommerce.product_service.service;

import com.ecommerce.product_service.config.KafkaTopicProperties;
import com.ecommerce.product_service.dto.ProductCreatedEvent;
import com.ecommerce.product_service.dto.ProductDeletedEvent;
import com.ecommerce.product_service.dto.ProductUpdatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ProductProducerServiceImp implements ProductProducerService {

    private final KafkaTopicProperties kafkaTopicProperties;
    private final KafkaTemplate<UUID, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final Logger logger = LogManager.getLogger(ProductProducerServiceImp.class);

    @Override
    public void sendProductCreationEvent(UUID productId, ProductCreatedEvent event) {
        logger.info("Sending ProductCreatedEvent to Kafka topic {}: key = {}, value = {}",
                kafkaTopicProperties.getProductCreated(), productId, event);

        try {
            String jsonMessage = objectMapper.writeValueAsString(event);

            CompletableFuture <SendResult<UUID, String>> completableFuture =
                    kafkaTemplate.send(kafkaTopicProperties.getProductCreated(), productId, jsonMessage);

            completableFuture.whenComplete((result, ex) -> {
                if (ex != null) {
                    // Handle the error
                    logger.error("Error sending ProductCreatedEvent to Kafka: {}", ex.getMessage());
                } else {
                    // Handle the success
                    logger.info("ProductCreatedEvent sent to Kafka topic {}: key = {}, value = {}",
                            kafkaTopicProperties.getProductCreated(), result.getProducerRecord().key(), result.getProducerRecord().value());
                }
            });

        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize ProductCreatedEvent: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to send ProductCreatedEvent to Kafka: {}", e.getMessage());
        }
    }

    @Override
    public void sendProductUpdateEvent(UUID productId, ProductUpdatedEvent event) {
        logger.info("Sending ProductUpdatedEvent to Kafka topic {}: key = {}, value = {}",
                kafkaTopicProperties.getProductUpdated(), productId, event);

        try {
            String jsonMessage = objectMapper.writeValueAsString(event);

            CompletableFuture <SendResult<UUID, String>> completableFuture =
                    kafkaTemplate.send(kafkaTopicProperties.getProductUpdated(), productId, jsonMessage);

            completableFuture.whenComplete((result, ex) -> {
                if (ex != null) {
                    // Handle the error
                    logger.error("Error sending ProductUpdatedEvent to Kafka: {}", ex.getMessage());
                } else {
                    // Handle the success
                    logger.info("ProductUpdatedEvent sent to Kafka topic {}: key = {}, value = {}",
                            kafkaTopicProperties.getProductUpdated(), result.getProducerRecord().key(), result.getProducerRecord().value());
                }
            });

        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize ProductUpdatedEvent: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to send ProductUpdatedEvent to kafka: {}", e.getMessage());
        }
    }

    @Override
    public void sendProductDeletionEvent(UUID productId, ProductDeletedEvent event) {
        logger.info("Sending ProductDeletedEvent to Kafka topic {}: key = {}, value = {}",
                kafkaTopicProperties.getProductDeleted(), productId, event);

        try {
            String jsonMessage = objectMapper.writeValueAsString(event);

            CompletableFuture <SendResult<UUID, String>> completableFuture =
                    kafkaTemplate.send(kafkaTopicProperties.getProductDeleted(), productId, jsonMessage);

            completableFuture.whenComplete((result, ex) -> {
                if (ex != null) {
                    // Handle the error
                    logger.error("Error sending ProductDeletedEvent to Kafka: {}", ex.getMessage());
                } else {
                    // Handle the success
                    logger.info("ProductDeletedEvent sent to Kafka topic {}: key = {}, value = {}",
                            kafkaTopicProperties.getProductDeleted(), result.getProducerRecord().key(), result.getProducerRecord().value());
                }
            });

        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize ProductDeletedEvent: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Failed to send ProductDeletedEvent to kafka: {}", e.getMessage());
        }
    }
}
