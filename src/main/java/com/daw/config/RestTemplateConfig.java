package com.daw.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Value("${product.server.username}")
    private String username;

    @Value("${product.server.password}")
    private String password;

    @Value("${product.server.url}")
    private String baseUrl;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .rootUri(baseUrl) // Configura la URL base aquí
            .basicAuthentication(username, password)
            .build();
    }
}