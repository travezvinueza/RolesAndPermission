package com.develop.backend.insfraestructure.controller;

import com.develop.backend.application.dto.ErrorDto;
import com.develop.backend.insfraestructure.exception.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDto> handleAuthenticationException (AuthenticationException exception) {
        ErrorDto errorDto = ErrorDto.builder()
                .message(exception.getMessage())
                .error(HttpStatus.UNAUTHORIZED)
                .status(HttpStatus.UNAUTHORIZED.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(errorDto.getStatus()).body(errorDto);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException (UserNotFoundException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .message(ex.getMessage())
                .error(HttpStatus.NOT_FOUND)
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(errorDto.getStatus()).body(errorDto);
    }

    @ExceptionHandler(PermissionNotFoundException.class)
    public ResponseEntity<ErrorDto> handlePermissionNotFoundException (PermissionNotFoundException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .message(ex.getMessage())
                .error(HttpStatus.NOT_FOUND)
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(errorDto.getStatus()).body(errorDto);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorDto> handleRoleNotFoundException (RoleNotFoundException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .message(ex.getMessage())
                .error(HttpStatus.NOT_FOUND)
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(errorDto.getStatus()).body(errorDto);
    }

    @ExceptionHandler(EmailSendNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEmailSendNotFoundException (EmailSendNotFoundException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .message(ex.getMessage())
                .error(HttpStatus.NOT_FOUND)
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(errorDto.getStatus()).body(errorDto);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCategoryNotFoundException (CategoryNotFoundException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .message(ex.getMessage())
                .error(HttpStatus.NOT_FOUND)
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(errorDto.getStatus()).body(errorDto);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFoundException (ProductNotFoundException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .message(ex.getMessage())
                .error(HttpStatus.NOT_FOUND)
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(errorDto.getStatus()).body(errorDto);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorDto> handleOrderNotFoundException (OrderNotFoundException ex) {
        ErrorDto errorDto = ErrorDto.builder()
                .message(ex.getMessage())
                .error(HttpStatus.NOT_FOUND)
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(errorDto.getStatus()).body(errorDto);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleValidationException(ValidationException exception) {
        ErrorDto errorDTO = ErrorDto.builder()
                .message(exception.getMessage())
                .error(HttpStatus.CONFLICT)
                .status(HttpStatus.CONFLICT.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(errorDTO.getStatus()).body(errorDTO);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException exception) {
        ErrorDto errorDTO = ErrorDto.builder()
                .message(exception.getMessage())
                .error(HttpStatus.METHOD_NOT_ALLOWED)
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(errorDTO.getStatus()).body(errorDTO);
    }

    @ExceptionHandler
    public ResponseEntity<List<ErrorDto>> handleConstraintViolationException(ConstraintViolationException exception) {
        List<ErrorDto> errorDTOList = exception.getConstraintViolations()
                .stream()
                .map(error -> ErrorDto.builder()
                        .message(error.getMessage())
                        .error(HttpStatus.BAD_REQUEST)
                        .status(HttpStatus.BAD_REQUEST.value())
                        .timestamp(LocalDateTime.now())
                        .build()
                )
                .toList();
        return ResponseEntity.status(errorDTOList.getFirst().getStatus()).body(errorDTOList);
    }

    @ExceptionHandler
    public ResponseEntity<List<ErrorDto>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<ErrorDto> errorDTOList = exception.getBindingResult().getAllErrors()
                .stream()
                .map(error -> ErrorDto.builder()
                        .message(error.getDefaultMessage())
                        .error(HttpStatus.BAD_REQUEST)
                        .status(HttpStatus.BAD_REQUEST.value())
                        .timestamp(LocalDateTime.now())
                        .build()
                )
                .toList();
        return ResponseEntity.status(errorDTOList.getFirst().getStatus()).body(errorDTOList);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handleEnumException(HttpMessageNotReadableException exception) {
        String errorDetails = "";
        if (exception.getCause() instanceof InvalidFormatException invalidFormatException && Objects.nonNull(invalidFormatException.getTargetType())
                && invalidFormatException.getTargetType().isEnum()) {
            errorDetails = String.format("Invalid enum value: '%s' for the field: '%s'. The value must be one of: %s.",
                    invalidFormatException.getValue(), invalidFormatException.getPath().getLast().getFieldName(),
                    Arrays.toString(invalidFormatException.getTargetType().getEnumConstants()));
        }
        ErrorDto errorDTO = ErrorDto.builder()
                .message(errorDetails)
                .error(HttpStatus.BAD_REQUEST)
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(errorDTO.getStatus()).body(errorDTO);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGenericException(Exception exception) {
        ErrorDto errorDto = ErrorDto.builder()
                .message(exception.getMessage())
                .error(HttpStatus.INTERNAL_SERVER_ERROR)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(errorDto.getStatus()).body(errorDto);
    }

}
