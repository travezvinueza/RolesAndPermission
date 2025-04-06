package com.develop.backend.insfraestructure.controller;

import com.develop.backend.application.dto.response.GenericResponse;
import com.develop.backend.application.dto.response.JwtResponse;
import com.develop.backend.application.dto.request.LoginReqDto;
import com.develop.backend.application.dto.request.RefreshTokenReqDto;
import com.develop.backend.application.dto.UserDto;
import com.develop.backend.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v3/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginReqDto loginReqDto) {
        return new ResponseEntity<>(authService.login(loginReqDto), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<GenericResponse<UserDto>> register(@RequestBody UserDto userDto) {
        GenericResponse<UserDto> response = authService.register(userDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshAccessToken(@RequestBody RefreshTokenReqDto refreshTokenReqDto) {
        return new ResponseEntity<>(authService.refreshAccessToken(refreshTokenReqDto), HttpStatus.OK);
    }

}