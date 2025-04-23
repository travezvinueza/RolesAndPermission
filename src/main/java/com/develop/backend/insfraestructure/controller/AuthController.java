package com.develop.backend.insfraestructure.controller;

import com.develop.backend.application.dto.response.JwtResponse;
import com.develop.backend.application.dto.request.LoginReqDto;
import com.develop.backend.application.dto.request.RefreshTokenReqDto;
import com.develop.backend.application.dto.UserDto;
import com.develop.backend.domain.service.AuthService;
import com.develop.backend.domain.service.FileUploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v3/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final FileUploadService fileUploadService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginReqDto loginReqDto) {
        return new ResponseEntity<>(authService.login(loginReqDto), HttpStatus.OK);
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> register(@Valid @RequestPart UserDto userDto,
                                            @RequestPart(required = false) MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileUrl = fileUploadService.uploadFile(file);
            userDto.setImageProfile(fileUrl);
        }
        return new ResponseEntity<>(authService.register(userDto), HttpStatus.CREATED);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshAccessToken(@RequestBody RefreshTokenReqDto refreshTokenReqDto) {
        return new ResponseEntity<>(authService.refreshAccessToken(refreshTokenReqDto), HttpStatus.OK);
    }

}