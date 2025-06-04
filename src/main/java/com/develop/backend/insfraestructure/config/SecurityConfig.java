package com.develop.backend.insfraestructure.config;

import com.develop.backend.domain.service.OurUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final OurUserDetailsService ourUserDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    private static final String[] WHITE_LIST_URL = { "/v3/test/**", "/v3/api-docs/**",
            "/swagger-resources", "/swagger-resources/**", "/configuration/ui", "/graphql", "/graphiql",
            "/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html",
            "/v3/auth/**", "/upload/**", "/authenticate", "/paypal/**", "/v3/cache/**" };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth-> auth
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .requestMatchers("/v3/category/**").hasAnyAuthority("READ_CATEGORY", "CREATE_CATEGORY", "UPDATE_CATEGORY", "DELETE_CATEGORY")
                        .requestMatchers("/v3/product/**").hasAnyAuthority("READ_PRODUCT", "CREATE_PRODUCT", "UPDATE_PRODUCT", "DELETE_PRODUCT")
                        .requestMatchers("/v3/order/**").hasAnyAuthority("READ_ORDER", "CREATE_ORDER", "UPDATE_ORDER", "DELETE_ORDER")
                        .requestMatchers("/v3/order-detail/**").hasAnyAuthority("READ_ORDER_DETAIL", "UPDATE_ORDER_DETAIL", "DELETE_ORDER_DETAIL")
                        .requestMatchers("/v3/user/**").hasAnyAuthority("READ_USER", "UPDATE_USER", "DELETE_USER")
                        .requestMatchers("/v3/user/**", "/v3/role/**", "/v3/permission/**", "/v3/category/**", "/v3/product/**", "/v3/order/**", "/v3/order-detail/**").hasAnyAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(manager->manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(ourUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
}
