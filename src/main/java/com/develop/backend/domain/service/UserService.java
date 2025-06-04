package com.develop.backend.domain.service;

import com.develop.backend.application.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    UserDto updateUser(UserDto userDto, MultipartFile newImage) throws IOException;

    void deleteUser(Long id);

    Page<UserDto> getAllUsers(String username, int page, int size);

    UserDto getUserById(Long id);

}
