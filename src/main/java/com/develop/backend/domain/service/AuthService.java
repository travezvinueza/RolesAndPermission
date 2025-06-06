package com.develop.backend.domain.service;

import com.develop.backend.application.dto.request.RefreshTokenReqDto;
import com.develop.backend.application.dto.request.RegisterReqDto;
import com.develop.backend.application.dto.response.JwtResponse;
import com.develop.backend.application.dto.request.LoginReqDto;
import com.develop.backend.application.dto.UserDto;

public interface AuthService {
    JwtResponse login(LoginReqDto loginReqDto);

    UserDto register(RegisterReqDto registerReqDto);

    JwtResponse refreshAccessToken(RefreshTokenReqDto refreshTokenReqDto);
}
