package com.ecommerce.catalog_service.service;

import com.ecommerce.catalog_service.dto.StockStatusUpdatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class StockConsumerServiceImpl implements StockConsumerService {

    private static final Logger logger = LogManager.getLogger(StockConsumerServiceImpl.class);

    private final ObjectMapper objectMapper;
    private final Validator validator;
    private final StockSyncService stockSyncService;

    @KafkaListener(
            topics = "${kafka.topic.stockStatusUpdated}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    @Override
    public void consumeStockStatusUpdatedEvent(String stockStatusUpdatedEvent) {
        logger.info("Received StockStatusUpdatedEvent: {}", stockStatusUpdatedEvent);

        try {
            StockStatusUpdatedEvent event = objectMapper.readValue(stockStatusUpdatedEvent, StockStatusUpdatedEvent.class);
            logger.info("StockStatusUpdatedEvent parsed: {}", event);

            Set<ConstraintViolation<StockStatusUpdatedEvent>> violations = validator.validate(event);
            if (!violations.isEmpty())
                throw new ConstraintViolationException(violations);

            stockSyncService.updateStockStatus(event);

        } catch (ConstraintViolationException e) {
            logger.error("Validation failed for StockStatusUpdatedEvent: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error processing StockStatusUpdatedEvent: {}", e.getMessage());
        }
    }
}
