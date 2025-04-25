package com.ecommerce.user_service.auth.config.filter;

import com.ecommerce.user_service.auth.dto.JwtUserDto;
import com.ecommerce.user_service.auth.service.JwtService;
import com.ecommerce.user_service.common.dto.ApiResponse;
import com.ecommerce.user_service.common.exception.ApiException;
import com.ecommerce.user_service.common.util.ResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private static final Logger logger = LogManager.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String authHeaderStart = "Bearer ";

        try {
            if (authHeader == null || !authHeader.startsWith(authHeaderStart)) {
                logger.error("Can't read auth header");
                filterChain.doFilter(request, response);
                return;
            }

            String token = authHeader.substring(authHeaderStart.length());

            if (jwtService.validateToken(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                JwtUserDto userDto = jwtService.extractUserDto(token);

                UserDetails userDetails = userDetailsService.loadUserByUsername(userDto.email());
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                logger.info(authenticationToken.toString());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                request.setAttribute("userId", userDto.userId());
            }

            filterChain.doFilter(request, response);
        } catch (ApiException e) {
            ResponseUtil.writeResponse(response, ApiResponse.failure("Token not valid"), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            ResponseUtil.writeResponse(response, ApiResponse.failure("Unauthorized request"), HttpStatus.UNAUTHORIZED);
        }
    }
}
