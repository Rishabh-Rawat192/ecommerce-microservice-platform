package com.ecommerce.product_service.config;

import com.ecommerce.product_service.dto.UserDto;
import com.ecommerce.product_service.enums.Role;
import com.ecommerce.product_service.service.ActiveSellerService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthHeaderFilter extends OncePerRequestFilter {

    private static final Logger logger = LogManager.getLogger(AuthHeaderFilter.class);
    private final ActiveSellerService activeSellerService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            String userId = request.getHeader("X-USER-ID");
            String email = request.getHeader("X-USER-EMAIL");
            String role  = request.getHeader("X-USER-ROLE");

            if (userId == null || email == null || role == null) {
                logger.error("Cant read auth headers.");
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Unauthorized");
                return;
            }

            if (!activeSellerService.isActive(UUID.fromString(userId))) {
                logger.error("Not a active seller.");
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Unauthorized");
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDto userDto = new UserDto(UUID.fromString(userId), email, Role.valueOf(role));
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userDto.role().name());
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDto, null, Collections.singleton(authority)
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            logger.info("Auth headers parsed successfully. User ID: {}, Email: {}, Role: {}", userId, email, role);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Error while parsing auth headers: {}", e.getMessage());
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
