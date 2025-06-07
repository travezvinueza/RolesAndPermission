package com.develop.backend.domain.service.impl;

import com.develop.backend.application.dto.TokenDto;
import com.develop.backend.domain.entity.Token;
import com.develop.backend.domain.entity.User;
import com.develop.backend.domain.repository.TokenRepository;
import com.develop.backend.domain.repository.UserRepository;
import com.develop.backend.domain.service.TokenService;
import com.develop.backend.infrastructure.util.JwtGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;
    private final JwtGenerator jwtGenerator;
    private final UserRepository userRepository;

    @Override
    public TokenDto generateTokenRefresh(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + username));

        tokenRepository.deleteByUser(user.getId());

        String refreshToken = jwtGenerator.generateRefreshToken(new HashMap<>(), user);
        Instant expirationDate = jwtGenerator.extractExpiration(refreshToken).toInstant();

        Token newToken = Token.builder()
                .refreshToken(refreshToken)
                .expirationDate(expirationDate)
                .user(user)
                .build();

        tokenRepository.save(newToken);

        return TokenDto.builder()
                .refreshToken(newToken.getRefreshToken())
                .expirationDate(newToken.getExpirationDate())
                .userId(user.getId())
                .build();
    }
}
