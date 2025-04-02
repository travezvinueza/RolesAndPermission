package com.develop.backend.application.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {
    private Long id;
    private String refreshToken;
    private Instant expirationDate;
    private Long userId;
}
