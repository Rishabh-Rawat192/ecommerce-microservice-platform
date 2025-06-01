package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.config.KafkaTopicProperties;
import com.ecommerce.inventory_service.dto.StockStatusUpdatedEvent;
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
public class InventoryProducerServiceImpl implements InventoryProducerService {

    private static final Logger logger = LogManager.getLogger(InventoryProducerServiceImpl.class);

    private final KafkaTopicProperties kafkaTopicProperties;
    private final KafkaTemplate<UUID, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void sendStockStatusUpdatedEvent(StockStatusUpdatedEvent event) {
        logger.info("Sending ProductCreatedEvent to Kafka topic {}: key = {}, value = {}",
                kafkaTopicProperties.getProductCreated(), event.productId(), event);

        try {
            String serializedEvent = objectMapper.writeValueAsString(event);

            CompletableFuture<SendResult<UUID, String>> completableFuture =
                    kafkaTemplate.send(kafkaTopicProperties.getStockStatusUpdated(), event.productId(), serializedEvent);

            completableFuture.whenComplete((result, ex) -> {
                if (ex != null) {
                    logger.error("Error sending StockStatusUpdatedEvent to Kafka: {}", ex.getMessage());
                } else {
                    logger.info("StockStatusUpdatedEvent sent to Kafka topic {}: key = {}, value = {}",
                            kafkaTopicProperties.getProductCreated(), result.getProducerRecord().key(), result.getProducerRecord().value());
                }
            });

        } catch (Exception e) {
            logger.error("Failed to send StockStatusUpdatedEvent to Kafka: {}", e.getMessage());
        }
    }
}
