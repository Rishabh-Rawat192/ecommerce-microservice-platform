package com.ecommerce.order_service.service;

import com.ecommerce.order_service.config.KafkaTopicProperties;
import com.ecommerce.order_service.dto.*;
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
public class OrderProducerServiceImpl implements OrderProducerService {

    private static final Logger logger = LogManager.getLogger(OrderProducerServiceImpl.class);
    private final KafkaTopicProperties kafkaTopicProperties;
    private final KafkaTemplate<UUID, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void sendOrderCreationFailedEvent(UUID orderId, OrderCreationFailedEvent event) {
        String eventName = "OrderCreationFailedEvent";
        String topicName = kafkaTopicProperties.getOrderCreationFailed();

        logger.info("Sending {} event to Kafka topic {}: key = {}, value = {}",
                eventName, topicName, orderId, event);

        sendEvent(topicName, orderId, event, eventName);
    }

    @Override
    public void sendOrderConfirmedEvent(UUID orderId, OrderConfirmedEvent event) {
        String eventName = "OrderConfirmedEvent";
        String topicName = kafkaTopicProperties.getOrderConfirmed();

        logger.info("Sending {} event to Kafka topic {}: key = {}, value = {}",
                eventName, topicName, orderId, event);

        sendEvent(topicName, orderId, event, eventName);
    }

    @Override
    public void sendOrderConfirmationFailedEvent(UUID orderId, OrderConfirmationFailedEvent event) {
        String eventName = "OrderConfirmationFailedEvent";
        String topicName = kafkaTopicProperties.getOrderConfirmationFailed();

        logger.info("Sending {} event to Kafka topic {}: key = {}, value = {}",
                eventName, topicName, orderId, event);

        sendEvent(topicName, orderId, event, eventName);
    }

    @Override
    public void sendOrderCancelledEvent(UUID orderId, OrderCancelledEvent event) {
        String eventName = "OrderCancelledEvent";
        String topicName = kafkaTopicProperties.getOrderCancelled();

        logger.info("Sending {} event to Kafka topic {}: key = {}, value = {}",
                eventName, topicName, orderId, event);

        sendEvent(topicName, orderId, event, eventName);
    }

    @Override
    public void sendOrderExpiredEvent(UUID orderId, OrderExpiredEvent event) {
        String eventName = "OrderExpired";
        String topicName = kafkaTopicProperties.getOrderExpired();

        logger.info("Sending {} event to Kafka topic {}: key = {}, value = {}",
                eventName, topicName, orderId, event);

        sendEvent(topicName, orderId, event, eventName);
    }

    private  <T> void sendEvent(String topicName, UUID key, T event, String eventName) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);

            CompletableFuture<SendResult<UUID, String>> completableFuture =
                    kafkaTemplate.send(topicName, key, eventJson);

            completableFuture.whenComplete((result, ex) -> {
                if (ex != null) {
                    logger.error("Error sending {} event to Kafka", eventName, ex);
                } else {
                    logger.info("Successfully sent {} to Kafka topic {}: key = {}, value = {}", eventName,
                            topicName, result.getProducerRecord().key(), result.getProducerRecord().value());
                }
            });

        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize {} event", eventName, e);
        } catch (Exception e) {
            logger.error("Failed to send {} event to Kafka", eventName, e);
        }
    }
}
