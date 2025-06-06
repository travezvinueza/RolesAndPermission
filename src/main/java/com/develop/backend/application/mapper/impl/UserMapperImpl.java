package com.develop.backend.application.mapper.impl;

import com.develop.backend.application.dto.RoleDto;
import com.develop.backend.application.dto.UserDto;
import com.develop.backend.application.dto.request.RegisterReqDto;
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
    public User toUser(RegisterReqDto registerReqDto, Set<Role> roles) {
        if (registerReqDto == null) {
            return null;
        }
        return User.builder()
                .username(registerReqDto.getUsername())
                .email(registerReqDto.getEmail())
                .password(passwordEncoder.encode(registerReqDto.getPassword()))
                .gender(registerReqDto.getGender())
                .imageProfile(registerReqDto.getImageProfile())
                .roles(roles)
                .build();
    }

    @Override
    public UserDto toUserDto(User newUser, String accessToken) {
        return UserDto.builder()
                .id(newUser.getId())
                .username(newUser.getUsername())
                .email(newUser.getEmail())
                .imageProfile(newUser.getImageProfile())
                .gender(newUser.getGender())
                .creationDate(newUser.getCreationDate())
                .roles(newUser.getRoles().stream()
                        .map(role ->  RoleDto.builder()
                                .id(role.getId())
                                .roleName(role.getRoleName())
                                .build())
                        .toList())
                .accessToken(accessToken)
                .build();
    }
}
