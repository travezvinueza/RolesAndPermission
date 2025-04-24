package com.develop.backend.insfraestructure.exception;

public class EmailSendNotFoundException extends RuntimeException{
    public EmailSendNotFoundException(String message) {
        super(message);
    }
}
