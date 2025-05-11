package com.ecommerce.user_service.seller.service;

import com.ecommerce.user_service.seller.config.KafkaTopicProperties;
import com.ecommerce.user_service.seller.dto.SellerStatusMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaProducerServiceImp implements KafkaProducerService{

    private final KafkaTemplate<UUID, SellerStatusMessage> kafkaTemplate;
    private final KafkaTopicProperties kafkaTopicProperties;

    @Override
    public void sendSellerStatusUpdate(UUID sellerId, boolean isActive) {
        SellerStatusMessage message = new SellerStatusMessage(isActive);
        CompletableFuture <SendResult<UUID, SellerStatusMessage>> completableFuture =
                kafkaTemplate.send(kafkaTopicProperties.getSellerStatusUpdated(), sellerId, message);

        completableFuture.whenComplete((result, ex) ->{
            if (ex != null) {
                // Handle the error
                System.err.println("Error sending message: " + ex.getMessage());
            } else {
                // Handle the success
                System.out.println("Message sent successfully: " + result.getProducerRecord().value());
            }
        });
    }
}
