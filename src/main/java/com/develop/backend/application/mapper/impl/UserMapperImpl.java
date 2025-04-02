package com.develop.backend.application.mapper.impl;

import com.develop.backend.application.dto.RoleDto;
import com.develop.backend.application.dto.UserDto;
import com.develop.backend.application.mapper.UserMapper;
import com.develop.backend.domain.entity.Role;
import com.develop.backend.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {
    private final PasswordEncoder passwordEncoder;

    @Override
    public User toUser(UserDto userDto, Set<Role> roles) {
        if (userDto == null) {
            return null;
        }
        return User.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .imageProfile(userDto.getImageProfile())
                .creationDate(userDto.getCreationDate())
                .roles(roles)
                .accountLocked(false)
                .build();
    }


    @Override
    public UserDto toUserDto(User newUser, String accessToken) {
        return UserDto.builder()
                .username(newUser.getUsername())
                .email(newUser.getEmail())
                .imageProfile(newUser.getImageProfile())
                .creationDate(newUser.getCreationDate())
                .roles(newUser.getRoles().stream()
                        .map(role ->  RoleDto.builder()
                                .id(role.getId())
                                .roleName(role.getRoleName())
                                .build())
                        .toList())
                .accountLocked(newUser.isAccountLocked())
                .accessToken(accessToken)    // Agregar el Access Token al DTO
                .build();
    }
}
