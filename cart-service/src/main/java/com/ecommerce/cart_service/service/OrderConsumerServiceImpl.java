package com.ecommerce.cart_service.service;

import com.ecommerce.cart_service.dto.OrderConfirmedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
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
public class OrderConsumerServiceImpl implements OrderConsumerService {

    private static final Logger logger = LogManager.getLogger(OrderConsumerServiceImpl.class);
    private final ObjectMapper objectMapper;
    private final Validator validator;
    private final CartService cartService;

    @KafkaListener(
            topics = "#{kafkaTopicProperties.orderConfirmed}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Override
    public void consumeOrderConfirmedEvent(String orderConfirmedEvent) {
        logger.info("Received Order Confirmed Event: {}", orderConfirmedEvent);

        try {
            OrderConfirmedEvent event = objectMapper.readValue(orderConfirmedEvent, OrderConfirmedEvent.class);

            Set<ConstraintViolation<OrderConfirmedEvent>> violations = validator.validate(event);
            if (!violations.isEmpty()) {
                logger.error("Validation errors for Order Confirmed Event: {}", violations);
                return;
            }

            cartService.deleteCart(event.userId());
        } catch (Exception e) {
            logger.error("Error processing Order Confirmed Event: {}", orderConfirmedEvent, e);
        }
    }
}
