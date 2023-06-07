package com.sam2n.backend.config.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.Set;

import static com.sam2n.backend.config.security.AuthorityType.USER;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf ->
                        conf
                                .requestMatchers("/error").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/actuator/**").permitAll()
                                .anyRequest().authenticated()
                )
                // Enable JWT Authentication
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(grantedAuthoritiesExtractor())))
                .build();
    }

    private JwtAuthenticationConverter grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
                    try {
                        List<String> strAuth = List.of(objectMapper.readValue(jwt.getClaimAsString("cognito:groups"), String[].class));
                        strAuth.forEach(log::debug);
                    } catch (JsonProcessingException e) {
                        log.error(e.getMessage(), e);
                    }
                    return Set.of(new SimpleGrantedAuthority("ROLE_" + USER.getClaimName()));
                }
        );
        return jwtAuthenticationConverter;
    }
}
