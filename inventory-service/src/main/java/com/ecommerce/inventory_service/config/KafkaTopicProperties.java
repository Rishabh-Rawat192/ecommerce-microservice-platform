package com.ecommerce.inventory_service.config;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "kafka.topic")
@Validated
@Getter
@Setter
public class KafkaTopicProperties {
    @NotNull(message = "The inventoryDeleted topic name must not be null.")
    private String productCreated;

    @NotNull(message = "The stockStatusUpdated topic name must not be null.")
    private String stockStatusUpdated;
}
