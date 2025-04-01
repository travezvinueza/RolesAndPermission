package com.develop.backend.domain.service;

import com.develop.backend.application.dto.JwtResponse;
import com.develop.backend.application.dto.LoginDto;
import com.develop.backend.application.dto.UserDto;
import org.springframework.security.core.Authentication;

public interface AuthService {
    JwtResponse login(LoginDto loginDto);

    UserDto register(UserDto userDto);

    JwtResponse refreshToken(Authentication authentication);
}
