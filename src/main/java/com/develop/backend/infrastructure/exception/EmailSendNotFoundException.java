package com.develop.backend.infrastructure.exception;

public class EmailSendNotFoundException extends RuntimeException{
    public EmailSendNotFoundException(String message) {
        super(message);
    }
}
