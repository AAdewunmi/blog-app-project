package com.springapplication.blogappproject.config;

import com.springapplication.blogappproject.repository.UserRepository;
import com.springapplication.blogappproject.security.JwtAuthenticationEntryPoint;
import com.springapplication.blogappproject.security.JwtAuthenticationFilter;
import com.springapplication.blogappproject.security.JwtTokenProvider;
import com.springapplication.blogappproject.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Configuration class responsible for defining and initializing security-related beans
 * such as custom user details service, JWT token provider, authentication filter, and
 * authentication entry point. These beans are essential for implementing authentication
 * and authorization using Spring Security in the application.
 *
 * This configuration ensures proper integration of JWT-based security mechanisms, user
 * data retrieval, and error handling for unauthorized access.
 */
@Configuration
public class SecurityBeansConfig {

    /**
     * Creates and provides a bean of type {@link CustomUserDetailsService}.
     * This bean is responsible for loading user-specific data, such as user details,
     * required for authentication and authorization in a Spring Security context.
     * The necessary user-related data is retrieved from the database via the {@link UserRepository}.
     *
     * @param userRepository the {@link UserRepository} used to fetch user-related data from the database
     * @return an instance of {@link CustomUserDetailsService} configured with the provided {@link UserRepository}
     */
    @Bean
    public CustomUserDetailsService customUserDetailsService(UserRepository userRepository) {
        return new CustomUserDetailsService(userRepository);
    }

    /**
     * Defines a Spring bean for the JwtTokenProvider, which is responsible for handling
     * the generation, parsing, and validation of JSON Web Tokens (JWT) in the application.
     *
     * The JwtTokenProvider is a key component in managing authentication and authorization
     * using tokens by providing methods to generate secure tokens, extract user information,
     * and validate the tokens' authenticity.
     *
     * @return a new instance of {@link JwtTokenProvider}, initialized as a Spring-managed bean
     */
    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    /**
     * Configures and initializes a {@code JwtAuthenticationFilter} bean for the application context.
     * This filter is responsible for intercepting HTTP requests, validating JSON Web Tokens (JWTs),
     * and populating the security context with authentication details for authenticated users.
     *
     * @param jwtTokenProvider the provider used to validate and extract information from JSON Web Tokens (JWTs)
     * @param userDetailsService the service used to load user-specific details (such as username and authorities)
     * @return an instance of {@code JwtAuthenticationFilter} configured with the specified dependencies
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider,
            UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
    }

    /**
     * Creates and returns a bean of type {@link JwtAuthenticationEntryPoint}.
     *
     * This method provides an instance of {@link JwtAuthenticationEntryPoint}, which
     * is used to handle unauthorized access attempts in Spring Security. The bean
     * is responsible for sending HTTP 401 Unauthorized responses along with an
     * appropriate error message when authentication fails or when unauthenticated
     * users try to access secured resources.
     *
     * @return a {@link JwtAuthenticationEntryPoint} instance for handling
     *         unauthorized access attempts in the security configuration
     */
    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint() {
        return new JwtAuthenticationEntryPoint();
    }
}


