package com.develop.backend.application.dto.request;

import com.develop.backend.application.dto.RoleDto;
import com.develop.backend.domain.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterReqDto {
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
    private List<RoleDto> roles;
}
