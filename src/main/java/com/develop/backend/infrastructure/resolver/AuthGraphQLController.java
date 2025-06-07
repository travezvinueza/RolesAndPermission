package com.develop.backend.infrastructure.resolver;

import com.develop.backend.application.dto.UserDto;
import com.develop.backend.application.dto.request.LoginReqDto;
import com.develop.backend.application.dto.request.RefreshTokenReqDto;
import com.develop.backend.application.dto.request.RegisterReqDto;
import com.develop.backend.application.dto.response.JwtResponse;
import com.develop.backend.domain.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class AuthGraphQLController {
    private final AuthService authService;

    @MutationMapping
    public JwtResponse login(@Argument("input") LoginReqDto input) {
        return authService.login(input);
    }

    @MutationMapping
    public UserDto register(@Argument("input") RegisterReqDto input) {
        return authService.register(input);
    }

    @MutationMapping
    public JwtResponse refreshToken(@Argument("input") RefreshTokenReqDto input) {
        return authService.refreshAccessToken(input);
    }
}
