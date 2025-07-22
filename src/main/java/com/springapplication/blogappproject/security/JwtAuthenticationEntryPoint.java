package com.springapplication.blogappproject.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * A custom implementation of the {@link AuthenticationEntryPoint} interface
 * to handle unauthorized access attempts in a Spring Security context.
 *
 * This class is used to send an HTTP 401 Unauthorized error response
 * along with an appropriate error message when authentication fails
 * or when an unauthenticated user attempts to access a secured resource.
 *
 * The {@link //commence} method is invoked whenever an {@link AuthenticationException}
 * is thrown within the application.
 *
 * This implementation is typically registered within the security configuration
 * to define the behavior for handling authentication failures.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Handles unauthorized access attempts by sending an HTTP 401 Unauthorized response
     * with an appropriate error message.
     *
     * This method is invoked whenever an {@link AuthenticationException} is thrown
     * in the application. It constructs a response indicating that the requested resource
     * requires authentication, and the user has not provided valid credentials.
     *
     * @param request the {@link HttpServletRequest} object containing the client's request information
     * @param response the {@link HttpServletResponse} object used to send the error response to the client
     * @param authException the {@link AuthenticationException} that triggered the unauthorized access handling
     * @throws IOException if an input or output error occurs while sending the error response
     * @throws ServletException if an error occurs during the processing of the request or response
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: " + authException.getMessage());
    }
}
