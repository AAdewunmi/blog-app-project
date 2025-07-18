package com.springapplication.blogappproject.service.impl;

import com.springapplication.blogappproject.payload.LoginDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

/**
 * Unit test class for {@code AuthServiceImpl}.
 * It verifies the behavior of the {@code AuthServiceImpl} methods, ensuring
 * that functionality such as user login operates as expected.
 * The tests use a combination of mocking and assertions to simulate behavior
 * and validate outcomes without relying on actual external dependencies.
 */
@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    /**
     * Instance of {@code AuthServiceImpl} initialized for unit testing.
     * This field is annotated with {@code @InjectMocks}, which enables injecting
     * mocked dependencies into this service during the test execution.
     * It provides mechanisms to test the authentication-related methods
     * such as user login, ensuring proper integration with mocked components.
     */
    @InjectMocks
    private AuthServiceImpl authService;

    /**
     * The {@code authenticationManager} mock is used to simulate the behavior of
     * an {@code AuthenticationManager} implementation within the unit tests for
     * the {@code AuthServiceImpl} class. This allows verification of the
     * authentication logic without requiring actual authentication infrastructure.
     *
     * This mock is injected into the test class to facilitate testing of methods
     * that depend on authentication processes.
     */
    @Mock
    private AuthenticationManager authenticationManager;

    /**
     * Tests the {@code login} method of {@code AuthServiceImpl} to ensure
     * that valid user credentials result in successful authentication and
     * the expected success message.
     *
     * This test verifies the following:
     * - A {@code LoginDto} containing valid credentials is correctly passed
     *   to the authentication process.
     * - The {@code AuthenticationManager}'s {@code authenticate} method
     *   is invoked with a valid {@code UsernamePasswordAuthenticationToken}.
     * - The method returns the success message "User Logged-In Successfully!".
     *
     * The test uses mocking to simulate the behavior of the
     * {@code AuthenticationManager}, ensuring the absence of external dependencies.
     */

    @Test
    public void testLogin_ValidCredentials_ReturnsSuccessMessage() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("testuser");
        loginDto.setPassword("password123");

        Authentication mockAuthentication = Mockito.mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);

        // Act
        String result = authService.login(loginDto);

        // Assert
        assertEquals("User Logged-In Successfully!", result);
        Mockito.verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}