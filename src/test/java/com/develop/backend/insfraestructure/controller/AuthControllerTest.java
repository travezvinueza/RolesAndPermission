package com.develop.backend.insfraestructure.controller;

import com.develop.backend.domain.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Test
    void login() {
    }

    @Test
    void register() {
    }

    @Test
    void refreshAccessToken() {
    }
}