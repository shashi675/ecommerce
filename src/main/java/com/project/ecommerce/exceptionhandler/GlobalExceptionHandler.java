package com.project.ecommerce.exceptionhandler;

import com.project.ecommerce.dto.ApiResponse;
import com.project.ecommerce.exception.CategoryAlreadyExitsException;
import com.project.ecommerce.exception.ResourceNotFoundException;
import com.project.ecommerce.exception.UserAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiResponse<>(false, e.getMessage(), null)
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiResponse<>(false, e.getMessage(), null)
        );
    }

    @ExceptionHandler(CategoryAlreadyExitsException.class)
    public ResponseEntity<ApiResponse<String>> handleCategoryAlreadyExitsException(CategoryAlreadyExitsException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiResponse<>(false, e.getMessage(), null)
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiResponse(false, e.getMessage(), null)
        );
    }

}
