package com.develop.backend.domain.service;

import com.develop.backend.application.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto updateUser(UserDto userDto);

    void deleteUser(Long id);

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

}
