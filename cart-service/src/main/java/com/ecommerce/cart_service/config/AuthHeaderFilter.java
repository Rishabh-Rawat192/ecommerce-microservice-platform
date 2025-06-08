package com.ecommerce.cart_service.config;

import com.ecommerce.cart_service.dto.Role;
import com.ecommerce.cart_service.dto.UserDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
public class AuthHeaderFilter extends OncePerRequestFilter {

    private static  final Logger logger = LogManager.getLogger(AuthHeaderFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String userId = request.getHeader("X-USER-ID");
            String email = request.getHeader("X-USER-EMAIL");
            String role = request.getHeader("X-USER-ROLE");

            if (userId == null || email == null || role == null) {
                logger.error("Cant read auth headers.");
                filterChain.doFilter(request, response);
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Unauthorized access.");
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

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Unauthorized access.");
        }
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
