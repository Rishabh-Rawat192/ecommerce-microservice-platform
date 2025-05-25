package com.ecommerce.catalog_service.exception;

import com.ecommerce.catalog_service.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(ApiException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(ApiResponse.failure(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGenericException(Exception e) {
        String errorMessage = "An unexpected error occurred. Please try again later.";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.failure(errorMessage));
    }
}
