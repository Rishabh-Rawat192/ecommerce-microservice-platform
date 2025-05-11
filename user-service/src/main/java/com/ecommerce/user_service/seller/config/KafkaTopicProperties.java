package com.ecommerce.user_service.seller.config;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
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

    @PostConstruct
    public void logProperties() {
        System.out.println("Seller Status Update Topic: " + sellerStatusUpdated);
    }
}
