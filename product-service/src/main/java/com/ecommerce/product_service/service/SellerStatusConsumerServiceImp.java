package com.ecommerce.product_service.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SellerStatusConsumerServiceImp implements SellerStatusConsumerService {

    private final static Logger logger =  LogManager.getLogger(SellerStatusConsumerServiceImp.class);

    @KafkaListener(
            topics = "${kafka.topic.sellerStatusUpdated}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Override
    public void consumeSellerStatusMessage(ConsumerRecord<UUID, String> record) {
        logger.info("Received message with key: {} and value: {}", record.key(), record.value());
    }
}
