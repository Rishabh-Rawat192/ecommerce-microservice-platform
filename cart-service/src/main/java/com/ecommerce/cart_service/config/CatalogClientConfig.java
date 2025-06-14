package com.ecommerce.cart_service.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class CatalogClientConfig {

    @Value("${catalog.service.url}")
    private String catalogServiceUrl;

    @PostConstruct
    public void validateUrl() {
        if (catalogServiceUrl == null || catalogServiceUrl.isBlank()) {
            throw new IllegalStateException("Missing or blank catalog.service.url configuration");
        }
    }

    @Bean
    public WebClient catalogWebClient() {
        return WebClient.builder()
                .baseUrl(catalogServiceUrl)
                .build();
    }
}
