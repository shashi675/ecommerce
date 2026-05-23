package com.project.ecommerce.exception;

public class CategoryAlreadyExitsException extends RuntimeException{
    public CategoryAlreadyExitsException(String message) {
        super(message);
    }
}
