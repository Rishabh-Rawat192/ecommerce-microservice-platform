package com.ecommerce.order_service.config;

import com.ecommerce.order_service.dto.UserDto;
import com.ecommerce.order_service.enums.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
public class AuthHeaderFilter extends OncePerRequestFilter {

    private static final Logger logger = LogManager.getLogger(AuthHeaderFilter.class);
    public static final String USER_ID_HEADER = "X-USER-ID";
    public static final String USER_EMAIL_HEADER = "X-USER-EMAIL";
    public static final String USER_ROLE_HEADER = "X-USER-ROLE";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userId = request.getHeader(USER_ID_HEADER);
        String email = request.getHeader(USER_EMAIL_HEADER);
        String role = request.getHeader(USER_ROLE_HEADER);

        try {
            if (userId == null || email == null || role == null) {
                logger.info("Auth header not found.");
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Unauthorized access.");
                return;
            }

            logger.debug("Received Headers - ID: {}, Email: {}, Role: {}", userId, email, role);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDto userDto = new UserDto(UUID.fromString(userId), email, Role.valueOf(role));

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userDto.role().name());
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                   userDto, null, Collections.singleton(authority)
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            logger.error("Error while parsing auth headers: {}", e.getMessage());
            sendErrorResponseSafely(response, HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status.value());
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }

    private void sendErrorResponseSafely(HttpServletResponse response, HttpStatus status, String message) {
        try {
            sendErrorResponse(response, status, message);
        } catch (IOException ioException) {
            logger.error("Failed to send error response: {}", ioException.getMessage());
        }
    }
}
