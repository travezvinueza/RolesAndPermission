package com.develop.backend.application.mapper;

import com.develop.backend.application.dto.UserDto;
import com.develop.backend.domain.entity.Role;
import com.develop.backend.domain.entity.User;

import java.util.Set;

public interface UserMapper {
    User toUser(UserDto userDto, Set<Role> roles);

    UserDto toUserDto(User newUser, String accessToken);
}
