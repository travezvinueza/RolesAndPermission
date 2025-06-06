package com.develop.backend.domain.service.impl;

import com.develop.backend.application.dto.UserDto;
import com.develop.backend.domain.entity.Role;
import com.develop.backend.domain.entity.User;
import com.develop.backend.domain.service.FileUploadService;
import com.develop.backend.domain.service.UserService;
import com.develop.backend.insfraestructure.exception.RoleNotFoundException;
import com.develop.backend.insfraestructure.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.develop.backend.application.mapper.UserMapper;
import com.develop.backend.domain.repository.RoleRepository;
import com.develop.backend.domain.repository.UserRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileUploadService fileUploadService;

    @Override
    public UserDto updateUser(UserDto userDto, MultipartFile newImage) throws IOException {
        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + userDto.getId()));

        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setGender(userDto.getGender());

        if (newImage != null && !newImage.isEmpty()) {
            if (user.getImageProfile() != null && !user.getImageProfile().isEmpty()) {
                fileUploadService.deleteUpload(user.getImageProfile());
            }
            String fileUrl = fileUploadService.uploadFile(newImage, "users");
            user.setImageProfile(fileUrl);
        }

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        Set<Role> roles = (userDto.getRoles() == null || userDto.getRoles().isEmpty())
                ? Set.of()
                : userDto.getRoles().stream()
                .map(roleDto -> roleRepository.findByRoleName(roleDto.getRoleName())
                        .orElseThrow(() -> new RoleNotFoundException("Rol no encontrado: " + roleDto.getRoleName())))
                .collect(Collectors.toSet());

        if (!roles.isEmpty()) {
            user.setRoles(roles);
        }

        return userMapper.toUserDto(userRepository.save(user), null);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found for delete"));
        userRepository.delete(user);
    }

    @Override
    public Page<UserDto> getAllUsers(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage;
        if (username != null && !username.isBlank()) {
            userPage = userRepository.findByUsernameContaining(username, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        return userPage.map(user -> userMapper.toUserDto(user, null));
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + id));
        return userMapper.toUserDto(user, null);
    }
}
