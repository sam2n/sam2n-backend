package com.sam2n.backend.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().ignoringRequestMatchers("/api/**");
        return http.build();
    }


    @RequiredArgsConstructor
    @Getter
    public enum Authority {
        USER("USER"),
        ADMIN("ADMIN");
        private final String name;
    }
}
