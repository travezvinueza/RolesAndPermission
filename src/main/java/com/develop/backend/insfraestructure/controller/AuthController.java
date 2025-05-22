package com.develop.backend.insfraestructure.controller;

import com.develop.backend.application.dto.response.JwtResponse;
import com.develop.backend.application.dto.request.LoginReqDto;
import com.develop.backend.application.dto.request.RefreshTokenReqDto;
import com.develop.backend.application.dto.UserDto;
import com.develop.backend.domain.service.AuthService;
import com.develop.backend.domain.service.EmailService;
import com.develop.backend.domain.service.FileUploadService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/v3/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final FileUploadService fileUploadService;
    private final EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginReqDto loginReqDto) {
        return new ResponseEntity<>(authService.login(loginReqDto), HttpStatus.OK);
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> register(@Valid @RequestPart UserDto userDto,
                                            @RequestPart(required = false) MultipartFile imageProfile) throws IOException {
        if (imageProfile != null && !imageProfile.isEmpty()) {
            String fileUrl = fileUploadService.uploadFile(imageProfile, "users");
            userDto.setImageProfile(fileUrl);
        }
        return new ResponseEntity<>(authService.register(userDto), HttpStatus.CREATED);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshAccessToken(@RequestBody RefreshTokenReqDto refreshTokenReqDto) {
        return new ResponseEntity<>(authService.refreshAccessToken(refreshTokenReqDto), HttpStatus.OK);
    }

//    @PostMapping(value = "/sendWithAttachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<String> sendEmailWithAttachment(
//            @RequestParam String to,
//            @RequestParam String subject,
//            @RequestParam String text,
//            @RequestParam MultipartFile filePath) throws MessagingException {
//
//            emailService.sendEmailWithAttachment(to, subject, text, filePath);
//            return ResponseEntity.accepted().body("Correo enviado (procesando en segundo plano)");
//
//    }

    @PostMapping(value = "/sendWithMultipleAttachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> enviarCorreo(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String text,
            @RequestParam List<MultipartFile> files) throws MessagingException {

        emailService.sendEmailWithMultipleAttachment(to, subject, text, files);
        return ResponseEntity.accepted().body("Correo enviado (procesando en segundo plano)");
    }

}