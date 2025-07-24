package com.springapplication.blogappproject.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

/**
 * Test class for validating the behavior of the {@link JwtAuthenticationFilter}.
 * It verifies various scenarios related to JWT authentication processing.
 */
@SpringBootTest
public class JwtAuthenticationFilterTest {

    /**
     * Tests the behavior of the {@code JwtAuthenticationFilter} when the "Authorization" header is absent in the HTTP request.
     *
     * This test ensures that:
     * 1. The {@code SecurityContextHolder} does not contain any authentication information, as no token is provided.
     * 2. The filter proceeds to invoke the next filter in the chain.
     *
     * Expected Outcome:
     * - The authentication in the security context is null.
     * - The {@code FilterChain#doFilter()} method is called exactly once.
     *
     * @throws ServletException if a servlet-related error occurs during the filter execution
     * @throws IOException if an input or output exception occurs during the filter execution
     */
    @Test
    void testShouldNotAuthenticateWithoutAuthorizationHeader() throws ServletException, IOException {
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    /**
     * Verifies that the {@code JwtAuthenticationFilter} does not authenticate a user with an invalid JWT token.
     *
     * This test ensures that:
     * 1. An invalid token in the "Authorization" header does not populate the {@code SecurityContextHolder}.
     * 2. The filter proceeds to invoke the next filter in the chain without interrupting processing.
     *
     * Expected Outcome:
     * - The authentication in the security context remains null.
     * - The {@code FilterChain#doFilter()} method is executed exactly once.
     *
     * @throws ServletException if a servlet-related error occurs during filter execution
     * @throws IOException if an input or output exception occurs during filter execution
     */
    @Test
    void testShouldNotAuthenticateWithInvalidToken() throws ServletException, IOException {
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer invalid_token");
        when(jwtTokenProvider.validateToken("invalid_token")).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    /**
     * Verifies that the {@code JwtAuthenticationFilter} successfully authenticates a user
     * when provided with a valid JWT token in the "Authorization" header.
     *
     * This test ensures that:
     * 1. The token provided in the "Authorization" header is validated correctly.
     * 2. The username is extracted from the valid token and user details are fetched.
     * 3. The filter proceeds to invoke the next filter in the chain after successful authentication.
     *
     * Expected Outcome:
     * - The following interactions occur exactly once:
     *   - Retrieving the "Authorization" header from the request.
     *   - Validating the JWT token.
     *   - Extracting the username from the token.
     *   - Loading the user details from the {@code UserDetailsService}.
     *   - Invoking the next filter in the chain.
     *
     * @throws ServletException if a servlet-related error occurs during filter execution
     * @throws IOException if an input or output exception occurs during filter execution
     */
    @Test
    void testShouldAuthenticateWithValidToken() throws ServletException, IOException {
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String validToken = "valid_token";
        String username = "testUser";
        UserDetails userDetails = new User(username, "password", new ArrayList<>());

        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(jwtTokenProvider.validateToken(validToken)).thenReturn(true);
        when(jwtTokenProvider.getUsername(validToken)).thenReturn(username);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(request, times(1)).getHeader("Authorization");
        verify(jwtTokenProvider, times(1)).validateToken(validToken);
        verify(jwtTokenProvider, times(1)).getUsername(validToken);
        verify(userDetailsService, times(1)).loadUserByUsername(username);
        verify(filterChain, times(1)).doFilter(request, response);
    }

    /**
     * Tests the behavior of the {@code JwtAuthenticationFilter} when the "Authorization" header
     * is present but does not follow the expected format (e.g., missing "Bearer " prefix).
     *
     * This test ensures that:
     * 1. The filter does not attempt to validate or extract information from an improperly formatted token.
     * 2. The filter chain continues processing without any authentication being set in the security context.
     *
     * Expected Outcome:
     * - The authentication in the security context remains null.
     * - The {@code FilterChain#doFilter()} method is called exactly once.
     *
     * @throws ServletException if a servlet-related error occurs during filter execution
     * @throws IOException if an input or output exception occurs during filter execution
     */
    @Test
    void testShouldIgnoreInvalidAuthorizationHeaderFormat() throws ServletException, IOException {
        JwtTokenProvider jwtTokenProvider = mock(JwtTokenProvider.class);
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("InvalidFormatToken");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(jwtTokenProvider, never()).validateToken(anyString());
        verify(jwtTokenProvider, never()).getUsername(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }

}