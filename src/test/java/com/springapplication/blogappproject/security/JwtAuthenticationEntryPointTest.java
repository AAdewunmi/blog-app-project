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


}