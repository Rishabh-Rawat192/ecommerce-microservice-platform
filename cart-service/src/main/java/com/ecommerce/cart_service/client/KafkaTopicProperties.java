package com.ecommerce.cart_service.client;

import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka.topic")
@Data
public class KafkaTopicProperties {
    @NotEmpty
    private String orderConfirmed;

    private static final Logger logger = LogManager.getLogger(KafkaTopicProperties.class);

    @PostConstruct
    public void logKafkaProperties() {
        logger.info(this.toString());
    }
}
