package com.develop.backend.infrastructure.resolver.input;

import com.develop.backend.application.dto.RoleDto;
import com.develop.backend.application.dto.UserDto;
import com.develop.backend.domain.enums.Gender;
import lombok.Data;

import java.util.List;

@Data
public class UpdateInput {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Gender gender;
    private String imageProfile;
    private List<RoleDto> roles;


    public static UserDto convertToUserDto(UpdateInput input) {
        if (input == null) {
            return null;
        }
        return UserDto.builder()
                .id(input.getId())
                .username(input.getUsername())
                .email(input.getEmail())
                .password(input.getPassword())
                .gender(input.getGender())
                .imageProfile(input.getImageProfile())
                .roles(input.getRoles() != null ? input.getRoles() : List.of())
                .build();
    }
}
