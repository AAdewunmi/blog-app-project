package com.springapplication.blogappproject.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * Test class for the {@link JwtAuthenticationEntryPoint}.
 *
 * This class is responsible for verifying the behavior of the {@link JwtAuthenticationEntryPoint},
 * which handles unauthorized access attempts by sending an HTTP 401 Unauthorized error response.
 *
 * The tests ensure that the {@link JwtAuthenticationEntryPoint#commence(HttpServletRequest, HttpServletResponse, AuthenticationException)}
 * method behaves as expected under various scenarios, such as handling of exceptions with
 * non-null messages and null messages.
 */
@SpringBootTest
class JwtAuthenticationEntryPointTest {

    /**
     * Tests the behavior of the {@link JwtAuthenticationEntryPoint#commence(HttpServletRequest, HttpServletResponse, AuthenticationException)}
     * method when handling unauthorized access attempts.
     *
     * This test verifies that the {@link JwtAuthenticationEntryPoint#commence(HttpServletRequest, HttpServletResponse, AuthenticationException)}
     * method sends an HTTP 401 Unauthorized error response with an appropriate error message.
     *
     * Key validations performed in this test include:
     * - Ensures the `sendError` method of the {@link HttpServletResponse} object is invoked with the correct
     *   HTTP status code ({@code HttpServletResponse.SC_UNAUTHORIZED}) and the expected error message.
     * - Confirms that the error message includes the text "Unauthorized" combined with the message from
     *   the provided {@link AuthenticationException}.
     *
     * This ensures that the {@link JwtAuthenticationEntryPoint} correctly implements the contract of the
     * {@link org.springframework.security.web.AuthenticationEntryPoint} interface to handle authentication errors.
     *
     * @throws IOException if an input or output error occurs while sending the error response
     * @throws ServletException if an error occurs during request or response processing
     */

    @Test
    void testCommence_SendsUnauthorizedErrorResponse() throws IOException, ServletException {
        // Arrange
        JwtAuthenticationEntryPoint entryPoint = new JwtAuthenticationEntryPoint();
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        AuthenticationException mockException = mock(AuthenticationException.class);

        when(mockException.getMessage()).thenReturn("Test unauthorized access");

        // Act
        entryPoint.commence(mockRequest, mockResponse, mockException);

        // Assert
        verify(mockResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Unauthorized: " + mockException.getMessage());
    }

    /**
     * Tests the behavior of the {@link JwtAuthenticationEntryPoint#commence(HttpServletRequest, HttpServletResponse, AuthenticationException)}
     * method when handling unauthorized access attempts where the {@link AuthenticationException#getMessage()} returns null.
     *
     * This test ensures that the method sends an HTTP 401 Unauthorized error response with a default message pattern
     * where "Unauthorized: null" is appended to the response.
     *
     * Key validations:
     * - Ensures the `sendError` method of the {@link HttpServletResponse} object is invoked with the correct
     *   HTTP status code ({@code HttpServletResponse.SC_UNAUTHORIZED}).
     * - Verifies that the error message correctly appends a null value in the absence of a specific exception message.
     *
     * This test confirms that the {@link JwtAuthenticationEntryPoint} implements a consistent behavior for edge cases where
     * an {@link AuthenticationException} does not contain a message.
     *
     * @throws IOException if an input or output error occurs while sending the error response
     * @throws ServletException if an error occurs during request or response processing
     */
    @Test
    void testCommence_NullExceptionMessage_SendsUnauthorizedErrorResponse() throws IOException, ServletException {
        // Arrange
        JwtAuthenticationEntryPoint entryPoint = new JwtAuthenticationEntryPoint();
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        AuthenticationException mockException = mock(AuthenticationException.class);

        when(mockException.getMessage()).thenReturn(null);

        // Act
        entryPoint.commence(mockRequest, mockResponse, mockException);

        // Assert
        verify(mockResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: null");
    }

}