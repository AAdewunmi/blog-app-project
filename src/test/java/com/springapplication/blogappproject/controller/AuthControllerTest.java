package com.springapplication.blogappproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springapplication.blogappproject.exception.GlobalExceptionHandler;
import com.springapplication.blogappproject.payload.LoginDto;
import com.springapplication.blogappproject.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(GlobalExceptionHandler.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService; // Use @Mock here

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // still needed for @Mock fields
    }

    @Test
    @DisplayName("Test: Login with valid credentials")
    public void testLoginWithValidCredentials() throws Exception {
        LoginDto loginDto = new LoginDto("testuser", "password");

        when(authService.login(any(LoginDto.class)))
                .thenReturn("User Logged-In Successfully!");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User Logged-In Successfully!"));
    }

    @Test
    @DisplayName("Test: Login with invalid credentials")
    public void testLoginWithInvalidCredentials() throws Exception {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("wronguser");
        loginDto.setPassword("wrongpassword");

        when(authService.login(any(LoginDto.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Test: Login with missing username")
    public void testLoginWithMissingUsername() throws Exception {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setPassword("password");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test: Login with missing password")
    public void testLoginWithMissingPassword() throws Exception {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("testuser");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test: Login with empty request body")
    public void testLoginWithEmptyRequestBody() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
