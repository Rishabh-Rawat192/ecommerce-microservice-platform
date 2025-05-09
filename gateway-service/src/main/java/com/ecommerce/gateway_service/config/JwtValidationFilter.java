package com.ecommerce.gateway_service.config;

import com.ecommerce.gateway_service.dto.JwtUserDto;
import com.ecommerce.gateway_service.service.JwtValidatorService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtValidationFilter implements GlobalFilter {
    private final JwtValidatorService jwtValidatorService;

    private final Logger logger = LogManager.getLogger(JwtValidatorService.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        if (path.startsWith("/api/v1/auth"))
            return chain.filter(exchange);

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String authHeaderStart = "Bearer ";

        if (authHeader == null || !authHeader.startsWith(authHeaderStart)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        try {
            JwtUserDto userDto = jwtValidatorService.extractUserDto(token);

            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-USER-ID", userDto.userId().toString())
                    .header("X-USER-EMAIL", userDto.email())
                    .header("X-ROLE", userDto.role().name())
                    .build();

            ServerWebExchange mutatedExchange = exchange.mutate()
                    .request(mutatedRequest).build();

            return chain.filter(mutatedExchange);
        } catch (Exception e) {
            logger.error(e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

    }
}
