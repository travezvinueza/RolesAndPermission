package com.develop.backend.application.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class ErrorDto {
    private String message;
    private HttpStatus error;
    private Integer status;
    private LocalDateTime timestamp;
}
