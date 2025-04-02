package com.develop.backend.domain.service;

import com.develop.backend.application.dto.TokenDto;


public interface TokenService {
    TokenDto generateTokenRefresh(String username);
}
