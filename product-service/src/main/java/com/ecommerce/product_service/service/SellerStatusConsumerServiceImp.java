package com.ecommerce.product_service.service;

import com.ecommerce.product_service.dto.SellerStatusMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SellerStatusConsumerServiceImp implements SellerStatusConsumerService {

    private final static Logger logger =  LogManager.getLogger(SellerStatusConsumerServiceImp.class);
    private final ActiveSellerService activeSellerService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${kafka.topic.sellerStatusUpdated}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Override
    public void consumeSellerStatusMessage(ConsumerRecord<UUID, String> record) {
        logger.info("Received message with key: {} and value: {}", record.key(), record.value());

        try {
            SellerStatusMessage sellerStatusMessage = objectMapper.readValue(record.value(), SellerStatusMessage.class);

            if (sellerStatusMessage.isActive()) {
                activeSellerService.save(record.key());
                logger.info("Seller with ID {} is now active.", record.key());
            } else {
                activeSellerService.delete(record.key());
                logger.info("Seller with ID {} is now inactive.", record.key());
            }

        } catch (Exception e) {
            logger.error("Failed to deserialize message: {}", e.getMessage());
        }
    }
}
