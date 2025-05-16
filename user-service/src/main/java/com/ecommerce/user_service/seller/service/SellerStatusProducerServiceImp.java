package com.ecommerce.user_service.seller.service;

import com.ecommerce.user_service.seller.config.KafkaTopicProperties;
import com.ecommerce.user_service.seller.dto.SellerStatusMessage;
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
public class SellerStatusProducerServiceImp implements SellerStatusProducerService {

    private final KafkaTemplate<UUID, String> kafkaTemplate;
    private final KafkaTopicProperties kafkaTopicProperties;

    private final ObjectMapper objectMapper;

    private static final Logger logger = LogManager.getLogger(SellerStatusProducerServiceImp.class);

    @Override
    public void sendSellerStatusUpdate(UUID sellerId, boolean isActive) {
        SellerStatusMessage message = new SellerStatusMessage(isActive);

        logger.info("Sending message to Kafka topic {}: key = {}, value = {}",
                kafkaTopicProperties.getSellerStatusUpdated(), sellerId, message);

        try {
            String jsonMessage = objectMapper.writeValueAsString(message);

            CompletableFuture <SendResult<UUID, String>> completableFuture =
                    kafkaTemplate.send(kafkaTopicProperties.getSellerStatusUpdated(), sellerId, jsonMessage);

            completableFuture.whenComplete((result, ex) ->{
                if (ex != null) {
                    // Handle the error
                    logger.error("Error sending message to Kafka: {}", ex.getMessage());
                } else {
                    // Handle the success
                    logger.info("Message sent to Kafka topic {}: key = {}, value = {}",
                            kafkaTopicProperties.getSellerStatusUpdated(), result.getProducerRecord().key(), result.getProducerRecord().value());
                }
            });
        } catch (JsonProcessingException e) {
            System.err.println("Failed to serialize SellerStatusMessage: " + e.getMessage());
        }
    }
}
