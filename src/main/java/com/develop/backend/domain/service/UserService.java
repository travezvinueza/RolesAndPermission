package com.develop.backend.domain.service;

import com.develop.backend.application.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    UserDto updateUser(Long id, UserDto userDto, MultipartFile newImage) throws IOException;

    void deleteUser(Long id);

    Page<UserDto> getAllUsers(String username, Pageable pageable);

    UserDto getUserById(Long id);

}
