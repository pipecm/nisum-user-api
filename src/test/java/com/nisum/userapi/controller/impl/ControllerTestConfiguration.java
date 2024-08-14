package com.nisum.userapi.controller.impl;

import com.nisum.userapi.auth.JwtUtil;
import com.nisum.userapi.auth.JwtUtilConfig;
import com.nisum.userapi.auth.JwtUtilParameters;
import com.nisum.userapi.config.ApiAuthenticationConfiguration;
import com.nisum.userapi.config.ApiSecurityConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.List;

@TestConfiguration
@Import({ApiAuthenticationConfiguration.class, ApiSecurityConfiguration.class, JwtUtil.class, JwtUtilConfig.class, JwtUtilParameters.class})
public class ControllerTestConfiguration {
    private static final String USER_EMAIL = "fulano@nisum.com";
    private static final String USER_PASSWORD = "fd07s9fd76d";

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username(USER_EMAIL)
                .password(USER_PASSWORD)
                .build();

        return new InMemoryUserDetailsManager(List.of(user));
    }
}