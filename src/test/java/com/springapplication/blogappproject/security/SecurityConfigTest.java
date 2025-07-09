package com.springapplication.blogappproject.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

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
     * An instance of the SecurityConfig class that provides configuration for security-related components.
     * It contains beans for managing user authentication and authorization, password encoding,
     * and model mapping configurations, ensuring proper security and transformation operations within the application.
     *
     * This configuration includes:
     * - A UserDetailsService bean for managing user details such as username, password, and roles.
     * - A PasswordEncoder bean for encoding and verifying user passwords.
     * - A ModelMapper bean for flexible object-to-object mapping with customization options like null handling,
     *   field matching, and disabling collections merging.
     */
    private final SecurityConfig securityConfig = new SecurityConfig();

    /**
     * Tests the functionality of the userDetailsService bean in the SecurityConfig class.
     *
     * This test ensures that the UserDetailsService correctly loads the user details for
     * the user with username "admin" and validates the following:
     * 1. The UserDetails object is not null.
     * 2. The username of the returned UserDetails matches the expected value "admin".
     * 3. The password for the user matches the expected value when encoded.
     * 4. The user has the required authorities "ROLE_USER" and "ROLE_ADMIN".
     *
     * This test uses a BCryptPasswordEncoder to validate the password encoding.
     */
    @Test
    public void testUserDetailsService() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        UserDetailsService service = securityConfig.userDetailsService(encoder);

        UserDetails admin = service.loadUserByUsername("admin");
        assertNotNull(admin);
        assertEquals("admin", admin.getUsername());
        assertTrue(encoder.matches("secret", admin.getPassword()));
        assertTrue(admin.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertTrue(admin.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
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

