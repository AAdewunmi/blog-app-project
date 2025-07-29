package com.springapplication.blogappproject.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Test configuration for security settings in a Spring Boot application.
 *
 * This class is used for testing purposes to configure a simplified security setup.
 * It utilizes Spring Security to define HTTP request authorization rules and disable
 * CSRF protection. The class is annotated with @TestConfiguration to mark it as
 * a configuration specifically for test cases, and @EnableWebSecurity to enable
 * web security for the application.
 */
@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

    /**
     * Configures and builds a security filter chain for the application.
     * This method defines the security policies, including specific authorization
     * rules for endpoints and disabling CSRF protection.
     *
     * The endpoint "/api/categories" requires users to have the "ADMIN" role, while all
     * other requests are permitted without authentication.
     *
     * @param http the {@link HttpSecurity} object used to configure web-based security
     *             for specific HTTP requests
     * @return the configured {@link SecurityFilterChain} instance
     * @throws Exception if an error occurs while constructing the security configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests()
                .requestMatchers("/api/categories").hasRole("ADMIN")
                .anyRequest().permitAll()
                .and()
                .csrf().disable();
        return http.build();
    }
}