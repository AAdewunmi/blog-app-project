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

}