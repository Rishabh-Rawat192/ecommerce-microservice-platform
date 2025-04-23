package com.ecommerce.user_service.common.util;

import com.ecommerce.user_service.common.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class ResponseUtil {
    private ResponseUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void writeResponse(HttpServletResponse response, ApiResponse<?> apiResponse,
                                     HttpStatus httpStatus) throws IOException {
        response.setStatus(httpStatus.value());
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getWriter(), apiResponse);
    }
}
