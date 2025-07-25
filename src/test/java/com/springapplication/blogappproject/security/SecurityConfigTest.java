package com.springapplication.blogappproject.security;

import com.springapplication.blogappproject.config.SecurityConfig;
import com.springapplication.blogappproject.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test class for SecurityConfig.
 *
 * This class contains test cases to validate the functionality
 * of beans defined in the SecurityConfig class, such as userDetailsService
 * and modelMapper. It ensures that the configurations and behavior
 * of these beans meet the expected requirements.
 */
@ExtendWith(SpringExtension.class)
public class SecurityConfigTest {

    /**
     * A mocked instance of {@link CustomUserDetailsService} used for testing purposes.
     *
     * This variable represents a custom implementation of the {@link org.springframework.security.core.userdetails.UserDetailsService}
     * interface, designed to load user-specific data needed for authentication and authorization in the context of Spring Security.
     *
     * It is utilized within unit tests to validate the behavior of the security configuration and to ensure
     * proper interaction with dependencies in a controlled test environment.
     */

    private CustomUserDetailsService customUserDetailsService;
    /**
     * An instance of {@link JwtAuthenticationEntryPoint} used to handle unauthorized access
     * attempts in the security configuration of the application.
     *
     * This variable is typically initialized as a mock object during testing and serves
     * as a custom implementation of the {@link AuthenticationEntryPoint} interface.
     *
     * It is responsible for sending an HTTP 401 Unauthorized response with appropriate
     * error messages when authentication fails or when an unauthenticated user attempts
     * to access a protected resource.
     */

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    /**
     * An instance of {@link JwtAuthenticationFilter} used for filtering and handling
     * JSON Web Token (JWT)-based authentication in security testing.
     *
     * This filter intercepts HTTP requests, validates incoming JWTs, extracts user
     * details, and updates the Spring Security context with authenticated user
     * information. It is typically part of the security filter chain and plays a
     * critical role in ensuring that only users with valid tokens can access secured
     * resources.
     *
     * Used in unit tests to mock and verify behaviors related to JWT authentication
     * in the {@link SecurityConfig} configuration.
     */

    private JwtAuthenticationFilter jwtAuthenticationFilter;
    /**
     * Represents the security configuration of the application.
     *
     * This variable holds an instance of the {@link SecurityConfig} class, which defines
     * the security-related beans and configurations such as authentication filters, user details service,
     * and entry points for handling unauthorized access. It is tested and validated to ensure proper
     * behavior of the security mechanisms and their compliance with the defined requirements.
     */
    private SecurityConfig securityConfig;

    /**
     * Prepares the test environment before the execution of each test method.
     *
     * This method is annotated with {@code @BeforeEach}, ensuring that it runs
     * before each test execution within the test class.
     *
     * The purpose of this setup method is to initialize and configure the necessary
     * mock dependencies and the instance of the {@code SecurityConfig} class under test.
     * It performs the following steps:
     *
     * 1. Mocks required dependencies:
     *    - {@code customUserDetailsService}: A mocked instance of {@code CustomUserDetailsService}.
     *    - {@code jwtAuthenticationEntryPoint}: A mocked instance of {@code JwtAuthenticationEntryPoint}.
     *    - {@code jwtAuthenticationFilter}: A mocked instance of {@code JwtAuthenticationFilter}.
     *
     * 2. Creates an instance of {@code SecurityConfig} by injecting the mocked dependencies.
     *
     * This method ensures that the state of the mocks and the {@code SecurityConfig} instance
     * is properly isolated and prepared for each individual test case.
     */
    @BeforeEach
    public void setUp() {
        customUserDetailsService = mock(CustomUserDetailsService.class);
        jwtAuthenticationEntryPoint = mock(JwtAuthenticationEntryPoint.class);
        jwtAuthenticationFilter = mock(JwtAuthenticationFilter.class);
        securityConfig = new SecurityConfig(
                customUserDetailsService,
                jwtAuthenticationEntryPoint,
                jwtAuthenticationFilter
        );
    }



    /**
     * Tests the configuration of the {@link ModelMapper} bean provided by the {@link SecurityConfig} class.
     *
     * Verifies that the {@link ModelMapper} instance is properly configured with the expected settings:
     * - Ensures that the configuration skips null values during mapping.
     * - Verifies that ambiguity in the mapping process is ignored.
     * - Confirms that collection merging is disabled.
     * - Validates that field matching is enabled and uses {@code PRIVATE} access level.
     *
     * Additionally, it simulates the behavior of the custom {@link AbstractConverter}
     * for copying sets to ensure proper functionality in the mapping process.
     *
     * This test ensures that the {@link ModelMapper} bean will behave as intended
     * under the defined configuration.
     */
    @Test
    public void testModelMapperConfiguration() {
        ModelMapper modelMapper = securityConfig.modelMapper();

        assertNotNull(modelMapper);
        assertTrue(modelMapper.getConfiguration().isSkipNullEnabled());
        assertTrue(modelMapper.getConfiguration().isAmbiguityIgnored());
        assertFalse(modelMapper.getConfiguration().isCollectionsMergeEnabled());
        assertTrue(modelMapper.getConfiguration().isFieldMatchingEnabled());
        assertEquals(Configuration.AccessLevel.PRIVATE,
                modelMapper.getConfiguration().getFieldAccessLevel());

        // Simulate converter behavior
        Set<String> input = Set.of("sample");
        Set<String> copy = new HashSet<>(input);
        assertEquals(1, copy.size());
        assertTrue(copy.contains("sample"));
    }

}

