package com.develop.backend.domain.service.impl;

import com.develop.backend.application.dto.request.RefreshTokenReqDto;
import com.develop.backend.application.dto.request.RegisterReqDto;
import com.develop.backend.application.dto.response.JwtResponse;
import com.develop.backend.application.dto.request.LoginReqDto;
import com.develop.backend.application.dto.TokenDto;
import com.develop.backend.application.dto.UserDto;
import com.develop.backend.application.mapper.UserMapper;
import com.develop.backend.domain.entity.Role;
import com.develop.backend.domain.entity.Token;
import com.develop.backend.domain.entity.User;
import com.develop.backend.domain.repository.RoleRepository;
import com.develop.backend.domain.repository.TokenRepository;
import com.develop.backend.domain.repository.UserRepository;
import com.develop.backend.domain.service.AuthService;
import com.develop.backend.domain.service.TokenService;
import com.develop.backend.insfraestructure.exception.RoleNotFoundException;
import com.develop.backend.insfraestructure.exception.TokenNotFoundException;
import com.develop.backend.insfraestructure.exception.UserNotFoundException;
import com.develop.backend.insfraestructure.util.JwtGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtGenerator jwtGenerator;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;

    @Override
    public JwtResponse login(LoginReqDto loginReqDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginReqDto.getIdentifier(),
                            loginReqDto.getPassword())
            );

            User user = (User) authentication.getPrincipal();
            String accessToken = jwtGenerator.generateToken(user);
            TokenDto refreshTokenDto = tokenService.generateTokenRefresh(user.getEmail());

            return new JwtResponse(accessToken, refreshTokenDto.getRefreshToken());
        }  catch (BadCredentialsException e) {
            throw new UserNotFoundException("Usuario o contraseña incorrectos.");
        }
    }

    @Override
    public UserDto register(RegisterReqDto registerReqDto) {
        if (userRepository.findByEmail(registerReqDto.getEmail()).isPresent()) {
            throw new UserNotFoundException("El usuario ya existe");
        }

        Role defaultRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("Rol predeterminado 'ROLE_USER' no encontrado"));

        Set<Role> roles = (registerReqDto.getRoles() == null || registerReqDto.getRoles().isEmpty())
                ? Set.of(defaultRole)
                : registerReqDto.getRoles().stream()
                .map(roleDto -> roleRepository.findByRoleName(roleDto.getRoleName())
                        .orElseThrow(() -> new RoleNotFoundException("Rol no encontrado: " + roleDto.getRoleName())))
                .collect(Collectors.toSet());

        User newUser = userMapper.toUser(registerReqDto, roles);
        User savedUser = userRepository.save(newUser);

        String accessToken =  jwtGenerator.generateToken(savedUser);

        return userMapper.toUserDto(savedUser, accessToken);
    }

    @Override
    public JwtResponse refreshAccessToken(RefreshTokenReqDto refreshTokenReqDto) {
        Token token = tokenRepository.findByRefreshToken(refreshTokenReqDto.getRefreshToken())
                .orElseThrow(() -> new TokenNotFoundException("Refresh Token inválido"));

        if (token.getExpirationDate().isBefore(Instant.now())) {
            throw new TokenNotFoundException("Refresh Token expirado");
        }

        User user = token.getUser();

        String newAccessToken = jwtGenerator.generateToken(user);

        return new JwtResponse(newAccessToken, token.getRefreshToken());
    }

}
