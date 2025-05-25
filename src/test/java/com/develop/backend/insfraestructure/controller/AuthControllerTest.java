package com.develop.backend.insfraestructure.controller;

import com.develop.backend.application.dto.UserDto;
import com.develop.backend.application.dto.request.LoginReqDto;
import com.develop.backend.application.dto.request.RefreshTokenReqDto;
import com.develop.backend.application.dto.response.JwtResponse;
import com.develop.backend.domain.enums.Gender;
import com.develop.backend.domain.service.AuthService;
import com.develop.backend.domain.service.EmailService;
import com.develop.backend.domain.service.FileUploadService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;


@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private FileUploadService fileUploadService;

    @Mock
    private EmailService emailService;

    @Test
    void login() {
        // 1. Create a LoginReqDto.
        LoginReqDto loginReqDto = LoginReqDto.builder().identifier("testuser").password("password").build();
        // 2. Create an expected JwtResponse.
        JwtResponse expectedResponse = new JwtResponse("accessToken", "refreshToken");
        // 3. Mock authService.login(any(LoginReqDto.class)) to return the expected JwtResponse.
        when(authService.login(any(LoginReqDto.class))).thenReturn(expectedResponse);
        // 4. Call authController.login() with the LoginReqDto.
        ResponseEntity<JwtResponse> responseEntity = authController.login(loginReqDto);
        // 5. Assert that the returned ResponseEntity contains the expected JwtResponse and HttpStatus.OK.
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        // 6. Verify that authService.login() was called once.
        verify(authService).login(any(LoginReqDto.class));
    }

    @Test
    void register_withoutImage() throws IOException {
        // 1. Create a UserDto for registration.
        UserDto userDto = UserDto.builder().username("testuser").email("test@example.com").password("password").gender(Gender.MASCULINE).build();
        // 2. Create an expected UserDto that would be returned by the service.
        UserDto expectedUserDto = UserDto.builder().id(1L).username("testuser").email("test@example.com").gender(Gender.MASCULINE).build();
        // 3. Mock authService.register(any(UserDto.class)) to return the expected UserDto.
        when(authService.register(any(UserDto.class))).thenReturn(expectedUserDto);
        // 4. Call authController.register() with the UserDto and null for MultipartFile.
        ResponseEntity<UserDto> responseEntity = authController.register(userDto, null);
        // 5. Assert that the returned ResponseEntity contains the expected UserDto and HttpStatus.CREATED.
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedUserDto, responseEntity.getBody());
        // 6. Verify that authService.register() was called once.
        verify(authService).register(any(UserDto.class));
        // 7. Verify that fileUploadService.uploadFile() was NOT called.
        verify(fileUploadService, never()).uploadFile(any(MultipartFile.class), any(String.class));
    }

    @Test
    void register_withImage() throws IOException {
        // 1. Create a UserDto for registration.
        UserDto userDto = UserDto.builder().username("testuser").email("test@example.com").password("password").gender(Gender.MASCULINE).build();
        // 2. Create a MockMultipartFile for the image.
        MockMultipartFile imageFile = new MockMultipartFile("imageProfile", "test.jpg", "image/jpeg", "test image content".getBytes());
        // 3. Define an expected file URL string.
        String expectedFileUrl = "http://example.com/test.jpg";
        // 4. Mock fileUploadService.uploadFile(any(MultipartFile.class), eq("users")) to return the expected file URL.
        when(fileUploadService.uploadFile(any(MultipartFile.class), eq("users"))).thenReturn(expectedFileUrl);
        // 5. Create an expected UserDto that would be returned by the service (this DTO will include the file URL).
        UserDto userDtoWithImageUrl = UserDto.builder().username("testuser").email("test@example.com").password("password").gender(Gender.MASCULINE).imageProfile(expectedFileUrl).build();
        UserDto expectedUserDto = UserDto.builder().id(1L).username("testuser").email("test@example.com").gender(Gender.MASCULINE).imageProfile(expectedFileUrl).build();
        // 6. Mock authService.register(any(UserDto.class)) to return this expected UserDto.
        when(authService.register(userDtoWithImageUrl)).thenReturn(expectedUserDto);
        // 7. Call authController.register() with the UserDto and the mock image.
        ResponseEntity<UserDto> responseEntity = authController.register(userDto, imageFile);
        // 8. Assert that the returned ResponseEntity contains the expected UserDto (with image URL) and HttpStatus.CREATED.
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedUserDto, responseEntity.getBody());
        // 9. Verify that fileUploadService.uploadFile() was called once.
        verify(fileUploadService).uploadFile(any(MultipartFile.class), eq("users"));
        // 10. Verify that authService.register() was called once with the UserDto that includes the file URL.
        verify(authService).register(userDtoWithImageUrl);
    }


    @Test
    void refreshAccessToken() {
        // 1. Create a RefreshTokenReqDto.
        RefreshTokenReqDto refreshTokenReqDto = new RefreshTokenReqDto("refreshToken");
        // 2. Create an expected JwtResponse.
        JwtResponse expectedResponse = new JwtResponse("newAccessToken", "newRefreshToken");
        // 3. Mock authService.refreshAccessToken(any(RefreshTokenReqDto.class)) to return the expected JwtResponse.
        when(authService.refreshAccessToken(any(RefreshTokenReqDto.class))).thenReturn(expectedResponse);
        // 4. Call authController.refreshAccessToken() with the RefreshTokenReqDto.
        ResponseEntity<JwtResponse> responseEntity = authController.refreshAccessToken(refreshTokenReqDto);
        // 5. Assert that the returned ResponseEntity contains the expected JwtResponse and HttpStatus.OK.
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        // 6. Verify that authService.refreshAccessToken() was called once.
        verify(authService).refreshAccessToken(any(RefreshTokenReqDto.class));
    }

    @Test
    void enviarCorreo() throws MessagingException {
        // 1. Define test inputs: to, subject, text, and a list of MockMultipartFile.
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test email body";
        MockMultipartFile file1 = new MockMultipartFile("files", "file1.txt", "text/plain", "file1 content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("files", "file2.txt", "text/plain", "file2 content".getBytes());
        List<MultipartFile> files = List.of(file1, file2);

        // 2. Mock emailService.sendEmailWithMultipleAttachment(...) to complete normally (doNothing).
        doNothing().when(emailService).sendEmailWithMultipleAttachment(eq(to), eq(subject), eq(text), eq(files));

        // 3. Call authController.enviarCorreo(...).
        ResponseEntity<String> responseEntity = authController.enviarCorreo(to, subject, text, files);

        // 4. Assert that the returned ResponseEntity has HttpStatus.ACCEPTED and the correct body message.
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals("Correo enviado (procesando en segundo plano)", responseEntity.getBody());

        // 5. Verify that emailService.sendEmailWithMultipleAttachment(...) was called once with the correct parameters.
        verify(emailService).sendEmailWithMultipleAttachment(eq(to), eq(subject), eq(text), eq(files));
    }
}