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

import static org.springframework.http.HttpMethod.GET;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final OurUserDetailsService ourUserDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    private static final String[] WHITE_LIST_URL = { "/v3/test/**", "/v3/api-docs/**",
            "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
            "/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html",
            "/v3/auth/**", "/upload/**", "/authenticate", "/cache/**" };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth-> auth
                        .requestMatchers(WHITE_LIST_URL).permitAll()
                        .requestMatchers(GET,"/v3/category/list").hasAnyAuthority("ROLE_CLIENT")
                        .requestMatchers("/v3/permission/**").hasAnyAuthority( "READ_PERMISSION", "WRITE_PERMISSION", "CREATE_PERMISSION", "EDIT_PERMISSION", "VIEW_PERMISSION", "DELETE_PERMISSION")
                        .requestMatchers("/v3/role/**").hasAnyAuthority("READ_ROLE", "WRITE_ROLE", "CREATE_ROLE", "EDIT_ROLE", "VIEW_ROLE", "DELETE_ROLE")
                        .requestMatchers("/v3/user/**").hasAnyAuthority( "READ_USER", "WRITE_USER", "CREATE_USER", "EDIT_USER", "VIEW_USER", "DELETE_USER")
                        .requestMatchers("/v3/role/**", "/v3/permission/**", "/v3/user/**").hasAnyAuthority("ROLE_ADMIN", "ALL_PERMISSIONS")
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
