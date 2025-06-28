package com.ecommerce.order_service.scheduler;

import com.ecommerce.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OrderExpiryScheduler {

    private static final Logger logger = LogManager.getLogger(OrderExpiryScheduler.class);
    private final OrderService orderService;

    @Value("${scheduler.orderExpiryThresholdMinutes}")
    private long expiryThresholdMinutes;

    @Scheduled(fixedDelayString = "${scheduler.orderExpiryIntervalMillis}")
    public void triggerExpiryJob() {
        logger.info("Starting order expiry job with threshold: {} minutes", expiryThresholdMinutes);
        try {
            LocalDateTime expiryThreshold = LocalDateTime.now().minusMinutes(expiryThresholdMinutes);
            orderService.expireStaleOrders(expiryThreshold);
            logger.info("Order expiry job completed successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during order expiry job: {}", e.getMessage(), e);
        }
    }
}
