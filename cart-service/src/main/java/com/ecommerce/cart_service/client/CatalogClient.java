package com.ecommerce.cart_service.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.UUID;


@Component
public class CatalogClient {

    private static final Logger logger = LogManager.getLogger(CatalogClient.class);
    private final WebClient webClient;

    public CatalogClient(@Qualifier("catalogWebClient") WebClient webClient) {
        this.webClient = webClient;
    }

    public boolean productExists(UUID productId) {
        try {
            webClient
                    .head()
                    .uri("/internal/catalog/products/{id}", productId)
                    .retrieve().toBodilessEntity()
                    .block();
            return true;
        } catch (WebClientResponseException.NotFound e) {
            logger.warn("Product not found: {}", productId);
            return false;
        }
    }
}
