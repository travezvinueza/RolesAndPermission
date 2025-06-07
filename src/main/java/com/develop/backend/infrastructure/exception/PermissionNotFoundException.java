package com.develop.backend.infrastructure.exception;

public class PermissionNotFoundException extends RuntimeException{
    public PermissionNotFoundException(String message) {
        super(message);
    }
}
