package com.develop.backend.insfraestructure.controller;

import com.develop.backend.application.dto.UserDto;
import com.develop.backend.application.dto.request.LoginReqDto;
import com.develop.backend.application.dto.request.RefreshTokenReqDto;
import com.develop.backend.application.dto.response.JwtResponse;
import com.develop.backend.domain.enums.Gender;
import com.develop.backend.domain.service.AuthService;
import com.develop.backend.domain.service.FileUploadService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private FileUploadService fileUploadService;

    @Test
    void login() {
        LoginReqDto loginReqDto = LoginReqDto.builder().identifier("testuser").password("password").build();
        JwtResponse expectedResponse = new JwtResponse("accessToken", "refreshToken");

        when(authService.login(any(LoginReqDto.class))).thenReturn(expectedResponse);
        ResponseEntity<JwtResponse> responseEntity = authController.login(loginReqDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(authService).login(any(LoginReqDto.class));
    }

    @Test
    void register() throws IOException {
        UserDto userDto = UserDto.builder().username("testuser").email("test@example.com").password("password").gender(Gender.MASCULINE).build();
        UserDto expectedUserDto = UserDto.builder().id(1L).username("testuser").email("test@example.com").gender(Gender.MASCULINE).build();

        when(authService.register(any(UserDto.class))).thenReturn(expectedUserDto);
        ResponseEntity<UserDto> responseEntity = authController.register(userDto, null);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedUserDto, responseEntity.getBody());

        verify(authService).register(any(UserDto.class));
        verify(fileUploadService, never()).uploadFile(any(MultipartFile.class), any(String.class));
    }

    @Test
    void refreshAccessToken() {
        RefreshTokenReqDto refreshTokenReqDto = new RefreshTokenReqDto("refreshToken");
        JwtResponse expectedResponse = new JwtResponse("newAccessToken", "newRefreshToken");

        when(authService.refreshAccessToken(any(RefreshTokenReqDto.class))).thenReturn(expectedResponse);
        ResponseEntity<JwtResponse> responseEntity = authController.refreshAccessToken(refreshTokenReqDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(authService).refreshAccessToken(any(RefreshTokenReqDto.class));
    }
}