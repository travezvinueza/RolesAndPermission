package com.develop.backend.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginReqDto {
    @NotBlank(message = "El campo 'identifier' no puede estar vacío")
    private String identifier;
    @NotBlank(message = "El campo 'password' no puede estar vacío")
    private String password;
}
