package com.develop.backend.domain.service.impl;

import com.develop.backend.application.dto.JwtResponse;
import com.develop.backend.application.dto.LoginDto;
import com.develop.backend.application.dto.UserDto;
import com.develop.backend.application.mapper.UserMapper;
import com.develop.backend.domain.entity.Role;
import com.develop.backend.domain.entity.User;
import com.develop.backend.domain.repository.RoleRepository;
import com.develop.backend.domain.repository.UserRepository;
import com.develop.backend.domain.service.AuthService;
import com.develop.backend.insfraestructure.exception.AccountLockedException;
import com.develop.backend.insfraestructure.exception.RoleNotFoundException;
import com.develop.backend.insfraestructure.exception.UserNotFoundException;
import com.develop.backend.insfraestructure.util.JwtGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    @Override
    public JwtResponse login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDto.getIdentifier(),
                            loginDto.getPassword())
            );

            User user = (User) authentication.getPrincipal();
            String accessToken = jwtGenerator.generateToken(user);

            return new JwtResponse(accessToken, user.getImageProfile());
        } catch (LockedException e) {
            throw new AccountLockedException("La cuenta está bloqueada. Contacta al administrador.");
        } catch (BadCredentialsException e) {
            throw new UserNotFoundException("Usuario o contraseña incorrectos.");
        }
    }

    @Override
    public UserDto register(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            throw new UserNotFoundException("El usuario ya existe");
        }

        Role defaultRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RoleNotFoundException("Rol predeterminado 'USER' no encontrado"));

        Set<Role> roles = (userDto.getRoles() == null || userDto.getRoles().isEmpty())
                ? Set.of(defaultRole)
                : userDto.getRoles().stream()
                .map(roleDto -> roleRepository.findByRoleName(roleDto.getRoleName())
                        .orElseThrow(() -> new RoleNotFoundException("Rol no encontrado: " + roleDto.getRoleName())))
                .collect(Collectors.toSet());

        User newUser = userMapper.toUser(userDto, roles);

        userRepository.save(newUser);

        return userMapper.toUserDto(newUser);

    }

    @Override
    public JwtResponse refreshToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String refreshToken = jwtGenerator.generateRefreshToken(new HashMap<>(), user);

        return new JwtResponse(refreshToken, user.getImageProfile());
    }
}
