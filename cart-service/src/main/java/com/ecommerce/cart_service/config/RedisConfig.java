package com.ecommerce.cart_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;


@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(@Value("${spring.redis.host}") String host,
                                                           @Value("${spring.redis.port}") int port,
                                                           @Value("${spring.redis.timeout}") long timeoutMillis) {
        RedisStandaloneConfiguration standaloneConfiguration = new RedisStandaloneConfiguration(host, port);

        LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(timeoutMillis))
                .build();

        return new LettuceConnectionFactory(standaloneConfiguration, clientConfiguration);
    }
}
