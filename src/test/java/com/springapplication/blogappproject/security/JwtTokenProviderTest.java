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




}