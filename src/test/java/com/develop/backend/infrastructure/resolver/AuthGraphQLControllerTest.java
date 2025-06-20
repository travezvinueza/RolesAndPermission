package com.develop.backend.infrastructure.resolver;

import com.develop.backend.domain.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@AutoConfigureGraphQlTester
class AuthGraphQLControllerTest {
    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private AuthService authService;

    @Test
    void login() {
    }

    @Test
    void register() {
    }

    @Test
    void refreshToken() {
    }
}