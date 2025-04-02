package com.develop.backend.domain.service;

import com.develop.backend.application.dto.request.RefreshTokenReqDto;
import com.develop.backend.application.dto.response.GenericResponse;
import com.develop.backend.application.dto.response.JwtResponse;
import com.develop.backend.application.dto.request.LoginReqDto;
import com.develop.backend.application.dto.UserDto;

public interface AuthService {
    JwtResponse login(LoginReqDto loginReqDto);

    GenericResponse<UserDto> register(UserDto userDto);

    JwtResponse refreshAccessToken(RefreshTokenReqDto refreshTokenReqDto);
}
