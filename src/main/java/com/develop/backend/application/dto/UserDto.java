package com.develop.backend.application.dto;

import com.develop.backend.domain.enums.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(message = "username is required and cannot be blank.")
    private String username;
    @Email
    @NotBlank(message = "email is required and cannot be blank.")
    private String email;
    @NotBlank(message = "password is required and cannot be blank.")
    private String password;
    @NotNull(message = "Gender is required.")
    private Gender gender;
    private String imageProfile;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS", timezone = "America/Guayaquil")
    private Timestamp creationDate;
    private List<RoleDto> roles;
    private List<OrderDto> orders;
    private String accessToken;

}
