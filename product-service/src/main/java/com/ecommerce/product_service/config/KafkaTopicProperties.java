package com.ecommerce.product_service.config;

import jakarta.persistence.PostUpdate;
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

    @NotEmpty(message = "The productCreated topic name must not be empty.")
    private String productCreated;

    private final static Logger logger = LogManager.getLogger(KafkaTopicProperties.class);

    @PostUpdate
    public void logProperties() {
        logger.info("Product Created Topic: {}", productCreated);
    }
}
