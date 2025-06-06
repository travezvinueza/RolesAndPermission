package com.develop.backend.insfraestructure.controller;

import com.develop.backend.application.dto.request.RegisterReqDto;
import com.develop.backend.application.dto.response.JwtResponse;
import com.develop.backend.application.dto.request.LoginReqDto;
import com.develop.backend.application.dto.request.RefreshTokenReqDto;
import com.develop.backend.application.dto.UserDto;
import com.develop.backend.domain.service.AuthService;
import com.develop.backend.domain.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/v3/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginReqDto loginReqDto) {
        return new ResponseEntity<>(authService.login(loginReqDto), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterReqDto registerReqDto) {
        return new ResponseEntity<>(authService.register(registerReqDto), HttpStatus.CREATED);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshAccessToken(@RequestBody RefreshTokenReqDto refreshTokenReqDto) {
        return new ResponseEntity<>(authService.refreshAccessToken(refreshTokenReqDto), HttpStatus.OK);
    }

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