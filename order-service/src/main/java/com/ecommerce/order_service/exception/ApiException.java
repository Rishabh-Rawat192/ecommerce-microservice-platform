package com.ecommerce.order_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {
    private HttpStatus httpStatus;

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.httpStatus = status;
    }
}
