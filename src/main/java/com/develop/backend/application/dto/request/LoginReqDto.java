package com.develop.backend.application.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginReqDto {
    private String identifier;
    private String password;
}
