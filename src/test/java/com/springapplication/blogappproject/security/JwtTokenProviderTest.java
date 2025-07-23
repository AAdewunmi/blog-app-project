package com.springapplication.blogappproject.security;

import com.springapplication.blogappproject.exception.BlogAPIException;
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

    /**
     * Tests the `validateToken` method with an expired JSON Web Token (JWT).
     *
     * This method verifies that the `validateToken` method correctly identifies and
     * rejects an expired JWT. It uses a mocked `Authentication` object to generate
     * a JWT with an expiration time set in the past, ensuring the token is expired
     * at the time of validation. The test expects an appropriate exception to be
     * thrown when trying to validate the expired token.
     *
     * Steps performed in the test:
     * - A mocked `Authentication` object is set up with a predefined username.
     * - A `JwtTokenProvider` instance is created and configured with the expiration
     *   time set to a past timestamp to generate an expired token.
     * - The expired token is passed to the `validateToken` method, which is expected
     *   to throw a `BlogAPIException`.
     * - The exception's message is validated to confirm it indicates the token expiration.
     *
     * Preconditions:
     * - An `Authentication` object with a valid username is mocked.
     * - A properly initialized `JwtTokenProvider` instance is used with a valid JWT secret.
     *
     * Postconditions:
     * - The `validateToken` method throws a `BlogAPIException` with the message
     *   "Expired JWT token" for the expired token.
     */
    @Test
    public void testValidateToken_expiredToken() {
        // Mock `Authentication`
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        // Create an instance of `JwtTokenProvider`
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        jwtTokenProvider.jwtSecret = jwtSecret;
        jwtTokenProvider.jwtExpirationDate = -1000; // Set expiration to past time

        // Generate an expired token
        String token = jwtTokenProvider.generateToken(authentication);

        // Validate the token and expect an exception
        BlogAPIException exception = org.junit.jupiter.api.Assertions.assertThrows(
                BlogAPIException.class,
                () -> jwtTokenProvider.validateToken(token),
                "Expected an exception for expired token."
        );

        assertEquals("Expired JWT token", exception.getMessage(), "The error message should indicate the token is expired.");
    }

    /**
     * Tests the `validateToken` method with a malformed JSON Web Token (JWT).
     *
     * This test verifies that the `validateToken` method properly detects and rejects
     * a malformed JWT. It validates that an appropriate exception (`BlogAPIException`)
     * is thrown with the correct error message when a malformed token is passed for validation.
     *
     * Steps performed in the test:
     * - A `JwtTokenProvider` instance is created and configured with the necessary properties.
     * - A malformed token string is provided as input.
     * - The `validateToken` method is invoked with the malformed token, expecting it to trigger a `BlogAPIException`.
     * - The exception message is validated to ensure it specifies that the token is invalid.
     *
     * Preconditions:
     * - A properly initialized `JwtTokenProvider` instance with a valid JWT secret and expiration configuration.
     *
     * Postconditions:
     * - The `validateToken` method throws a `BlogAPIException` with the message "Invalid JWT Token"
     *   for the malformed token.
     */
    @Test
    public void testValidateToken_malformedToken() {
        // Create an instance of `JwtTokenProvider`
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
        jwtTokenProvider.jwtSecret = jwtSecret;
        jwtTokenProvider.jwtExpirationDate = jwtExpirationDate;

        // Provide a malformed token
        String malformedToken = "malformed.token.value";

        // Validate the token and expect an exception
        BlogAPIException exception = org.junit.jupiter.api.Assertions.assertThrows(
                BlogAPIException.class,
                () -> jwtTokenProvider.validateToken(malformedToken),
                "Expected an exception for malformed token."
        );

        assertEquals("Invalid JWT Token", exception.getMessage(), "The error message should indicate the token is malformed.");
    }
}