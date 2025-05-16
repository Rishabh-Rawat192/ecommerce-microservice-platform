package com.ecommerce.user_service.seller.config;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "kafka.topic")
@Validated
@Getter
@Setter
public class KafkaTopicProperties {

    @NotEmpty(message = "The sellerStatusUpdate topic name must not be empty.")
    private String sellerStatusUpdated;

    private static final Logger logger = LogManager.getLogger(KafkaTopicProperties.class);

    @PostConstruct
    public void logProperties() {
        logger.info("Seller Status Update Topic: {}", sellerStatusUpdated);
    }
}
