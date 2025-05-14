package com.ecommerce.user_service.seller.service;

import com.ecommerce.user_service.seller.config.KafkaTopicProperties;
import com.ecommerce.user_service.seller.dto.SellerStatusMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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

    @Override
    public void sendSellerStatusUpdate(UUID sellerId, boolean isActive) {
        SellerStatusMessage message = new SellerStatusMessage(isActive);

        try {
            String jsonMessage = objectMapper.writeValueAsString(message);

            CompletableFuture <SendResult<UUID, String>> completableFuture =
                    kafkaTemplate.send(kafkaTopicProperties.getSellerStatusUpdated(), sellerId, jsonMessage);

            completableFuture.whenComplete((result, ex) ->{
                if (ex != null) {
                    // Handle the error
                    System.err.println("Error sending message: " + ex.getMessage());
                } else {
                    // Handle the success
                    System.out.println("Message sent successfully: " + result.getProducerRecord().key() +","+ result.getProducerRecord().value());
                }
            });
        } catch (JsonProcessingException e) {
            System.err.println("Failed to serialize SellerStatusMessage: " + e.getMessage());
        }
    }
}
