package com.sam2n.backend.config.security;


import org.springframework.boot.actuate.autoconfigure.web.server.ManagementServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(ManagementServerProperties managementServerProperties) {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        // swagger ui
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:" + managementServerProperties.getPort()));

        corsConfiguration.setAllowedHeaders(
                List.of(
                        ORIGIN,
                        ACCESS_CONTROL_ALLOW_ORIGIN,
                        CONTENT_TYPE,
                        ACCEPT,
                        AUTHORIZATION,
                        ORIGIN, ACCEPT,
                        ACCESS_CONTROL_REQUEST_METHOD,
                        ACCESS_CONTROL_REQUEST_HEADERS
                )
        );
        corsConfiguration.setExposedHeaders(
                List.of(
                        ORIGIN,
                        CONTENT_TYPE,
                        ACCEPT,
                        AUTHORIZATION,
                        ACCESS_CONTROL_ALLOW_ORIGIN,
                        ACCESS_CONTROL_ALLOW_ORIGIN,
                        ACCESS_CONTROL_ALLOW_CREDENTIALS
                )
        );
        corsConfiguration.setAllowedMethods(List.of(GET.name(), POST.name(), PUT.name(), DELETE.name(), OPTIONS.name()));

        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
