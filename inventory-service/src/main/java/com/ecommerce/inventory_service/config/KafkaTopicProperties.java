package com.ecommerce.inventory_service.config;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "kafka.topic")
@Validated
@Data
public class KafkaTopicProperties {
    @NotEmpty
    private String productCreated;
    @NotEmpty
    private String stockStatusUpdated;

    @NotEmpty
    private String orderCreationFailed;
    @NotEmpty
    private String orderConfirmationFailed;
    @NotEmpty
    private String orderCancelled;
    @NotEmpty
    private String orderExpired;

    private static final Logger logger = LogManager.getLogger(KafkaTopicProperties.class);

    @PostConstruct
    public void logProperties() {
        logger.info(this.toString());
    }
}
