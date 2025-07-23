package com.springapplication.blogappproject.security;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@code JwtTokenProvider} class.
 *
 * The {@code JwtTokenProviderTest} class tests the functionality related to
 * generating and interpreting JSON Web Tokens (JWT) using the methods provided
 * by the {@code JwtTokenProvider} class. It utilizes Spring's integration testing
 * framework and Mockito to simulate authentication scenarios.
 *
 * Configurations for JWT secret key and expiration time are injected
 * from test properties, and various scenarios are validated using assertions.
 */
@SpringBootTest
public class JwtTokenProviderTest {

    /**
     * Represents the JWT secret key used for encoding and decoding JSON Web Tokens (JWT).
     *
     * The secret key is configured as a Base64-encoded value and injected from the application
     * properties using the key {@code app.jwt-secret}. It serves as a cryptographic key to
     * sign and verify the integrity of JWTs, ensuring the tokens are authentic and tamper-proof.
     *
     * It is used in the JWT generation and validation process to secure API authentication
     * and authorization mechanisms.
     *
     * Note: For security reasons, this key must be kept confidential, and a strong, random
     * value should be used in production environments.
     */
    @Value("${app.jwt-secret}")
    private String jwtSecret = "dGVzdHNlY3JldGtleXdpdGgxMjM0NTZjaGFyYWN0ZXJz"; // Test secret key (Base64-encoded)

    /**
     * The `jwtExpirationDate` variable specifies the duration, in milliseconds, for which a JSON Web Token (JWT)
     * remains valid after being issued. This value is used to set the expiration time of the token during its
     * creation and is relevant for handling user authentication and session management within the application.
     *
     * The value of this variable is injected from the application properties configuration using the
     * `@Value` annotation, allowing customization for different environments without changing the source code.
     *
     * Default Value: `3600000` (1 hour in milliseconds)
     */
    @Value("${app-jwt-expiration-milliseconds}")
    private long jwtExpirationDate = 3600000; // Mock expiration value (1 hour in milliseconds)

    /**
     * Tests the `generateToken` method of the `JwtTokenProvider` class.
     *
     * This test verifies the functionality of generating a JSON Web Token (JWT)
     * for a given `Authentication` object and performs the following checks:
     *
     * - Ensures the token is not null after generation.
     * - Validates that the token contains the correct username extracted from the
     *   provided `Authentication` object.
     *
     * The method uses Mockito to mock an `Authentication` object and configures
     * necessary details such as the username. Custom assertions provide validation
     * of the generated token's correctness.
     *
     * Dependencies involved in testing include the JWT secret and expiration time,
     * which are configured in the test environment.
     *
     * Preconditions: A valid `Authentication` object is required for token generation.
     * Postconditions: A non-null token is generated and validated against expected properties.
     */
    @Test
    public void testGenerateToken() {
        // Mock `Authentication`
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        // Create an instance of `JwtTokenProvider`
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        jwtTokenProvider.jwtSecret = jwtSecret;
        jwtTokenProvider.jwtExpirationDate = jwtExpirationDate;

        // Generate a token
        String token = jwtTokenProvider.generateToken(authentication);

        // Assertions
        assertNotNull(token, "Token should not be null");
        assertEquals("testUser", jwtTokenProvider.getUsername(token), "Token should contain the username");
    }

    /**
     * Tests the `getUsername` method with a valid JSON Web Token (JWT).
     *
     * This test ensures that the `getUsername` method correctly extracts the username
     * embedded within a valid JWT. It performs the following steps:
     *
     * - Mocks an `Authentication` object and sets a username.
     * - Uses the `generateToken` method to create a JWT with the mocked authentication details.
     * - Calls the `getUsername` method on the generated token.
     * - Verifies that the extracted username matches the expected value.
     *
     * Preconditions:
     * - A valid `Authentication` object with a username is mocked.
     * - A properly configured `JwtTokenProvider` instance is used.
     *
     * Postconditions:
     * - The username extracted from the token matches the username used during token generation.
     */
    @Test
    public void testGetUsername_validToken() {
        // Mock `Authentication`
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("validUser");

        // Create an instance of `JwtTokenProvider`
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        jwtTokenProvider.jwtSecret = jwtSecret;
        jwtTokenProvider.jwtExpirationDate = jwtExpirationDate;

        // Generate a token
        String token = jwtTokenProvider.generateToken(authentication);

        // Verify `getUsername` extracts the correct username
        String extractedUsername = jwtTokenProvider.getUsername(token);
        assertEquals("validUser", extractedUsername, "The extracted username should match the one used to generate the token.");
    }

    /**
     * Tests the `validateToken` method with a valid JSON Web Token (JWT).
     *
     * This test ensures that the `validateToken` method returns `true` for a properly
     * generated and valid token. It uses the `generateToken` method to create the token
     * and then validates its correctness.
     *
     * Preconditions:
     * - A valid `Authentication` object with a username is mocked.
     * - A properly configured `JwtTokenProvider` instance is used.
     *
     * Postconditions:
     * - The `validateToken` method returns `true` for the valid token.
     */
    @Test
    public void testValidateToken_validToken() {
        // Mock `Authentication`
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("validUser");

        // Create an instance of `JwtTokenProvider`
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        jwtTokenProvider.jwtSecret = jwtSecret;
        jwtTokenProvider.jwtExpirationDate = jwtExpirationDate;

        // Generate a valid token
        String token = jwtTokenProvider.generateToken(authentication);

        // Validate the token
        boolean isValid = jwtTokenProvider.validateToken(token);

        assertEquals(true, isValid, "The valid token should be validated successfully.");
    }
}