package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.dto.OrderCancelledEvent;
import com.ecommerce.inventory_service.dto.OrderConfirmationFailedEvent;
import com.ecommerce.inventory_service.dto.OrderCreationFailedEvent;
import com.ecommerce.inventory_service.dto.OrderExpiredEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import jakarta.validation.Validator;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderConsumerServiceImpl implements OrderConsumerService {

    private static final Logger logger = LogManager.getLogger(OrderConsumerServiceImpl.class);

    private final StockService stockService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @KafkaListener(
            topics = "#{kafkaTopicProperties.orderCreationFailed}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Override
    public void consumeOrderCreationFailedEvent(String orderCreationFailedEvent) {
        logger.info("Received Order Creation Failed Event: {}", orderCreationFailedEvent);

        try {
            OrderCreationFailedEvent event = objectMapper.readValue(orderCreationFailedEvent, OrderCreationFailedEvent.class);
            logger.info("Parsed Order Creation Failed Event: {}", event);

            Set<ConstraintViolation<OrderCreationFailedEvent>> violations = validator.validate(event);
            if (!violations.isEmpty()) {
                logger.error("Validation failed for OrderCreationFailedEvent: {}", violations);
                return;
            }

            stockService.rollbackReservation(event);
        } catch (Exception e) {
            logger.error("Unexpected error processing Order Creation Failed Event", e);
        }
    }

    @KafkaListener(
            topics = "#{kafkaTopicProperties.orderConfirmationFailed}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Override
    public void consumeOrderConfirmationFailedEvent(String orderConfirmationFailedEvent) {
        logger.info("Received Order Confirmation Failed Event: {}", orderConfirmationFailedEvent);

        try {
            OrderConfirmationFailedEvent event = objectMapper.readValue(orderConfirmationFailedEvent, OrderConfirmationFailedEvent.class);
            logger.info("Parsed Order Confirmation Failed Event: {}", event);

            Set<ConstraintViolation<OrderConfirmationFailedEvent>> violations = validator.validate(event);
            if (!violations.isEmpty()) {
                logger.error("Validation failed for OrderConfirmationFailedEvent: {}", violations);
                return;
            }

            stockService.rollbackReservationConfirmation(event);
        } catch (Exception e) {
            logger.error("Unexpected error processing ", e);
        }
    }

    @KafkaListener(
            topics = "#{kafkaTopicProperties.orderCancelled}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Override
    public void consumeOrderCancelledEvent(String orderCancelledEvent) {
        logger.info("Received Order Cancelled Event: {}", orderCancelledEvent);

        try {
            OrderCancelledEvent event = objectMapper.readValue(orderCancelledEvent, OrderCancelledEvent.class);
            logger.info("Parsed Order Cancelled Event: {}", event);

            Set<ConstraintViolation<OrderCancelledEvent>> violations = validator.validate(event);
            if (!violations.isEmpty()) {
                logger.error("Validation failed for OrderCancelledEvent: {}", violations);
                return;
            }

            stockService.cancelReservation(event);
        } catch (Exception e) {
            logger.error("Unexpected error processing Order Cancelled Event: {}", e.getMessage());
        }
    }

    @KafkaListener(
            topics = "#{kafkaTopicProperties.orderExpired}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Override
    public void consumeOrderExpiredEvent(String orderExpiredEvent) {
        logger.info("Received Order Expired Event: {}", orderExpiredEvent);

        try {
            OrderExpiredEvent event = objectMapper.readValue(orderExpiredEvent, OrderExpiredEvent.class);
            logger.info("Parsed Order Expired Event: {}", event);

            Set<ConstraintViolation<OrderExpiredEvent>> violations = validator.validate(event);
            if (!violations.isEmpty()) {
                logger.error("Validation failed for OrderExpiredEvent: {}", violations);
                return;
            }

            stockService.expireReservation(event);
        } catch (Exception e) {
            logger.error("Unexpected error processing Order Expired Event: {}", e.getMessage());
        }
    }
}
