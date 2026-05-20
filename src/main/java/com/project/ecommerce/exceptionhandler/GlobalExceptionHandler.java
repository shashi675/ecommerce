package com.project.ecommerce.exceptionhandler;

import com.project.ecommerce.dto.ApiResponse;
import com.project.ecommerce.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleUserNameNotFoundException(UserAlreadyExistsException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiResponse<>(false, e.getMessage(), null)
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiResponse(false, e.getMessage(), null)
        );
    }

}
