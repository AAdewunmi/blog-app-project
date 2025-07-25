package com.springapplication.blogappproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springapplication.blogappproject.exception.GlobalExceptionHandler;
import com.springapplication.blogappproject.payload.LoginDto;
import com.springapplication.blogappproject.payload.RegisterDto;
import com.springapplication.blogappproject.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the AuthController, responsible for testing the authentication-related
 * REST API endpoints such as login functionality. Utilizes MockMvc for testing the API
 * layer behavior and validates responses based on various scenarios.
 */
@WebMvcTest(AuthController.class)
@Import(GlobalExceptionHandler.class)
public class AuthControllerTest {

    /**
     * MockMvc instance used to perform HTTP requests in test cases.
     * Facilitates testing the web layer of the application by simulating HTTP requests and responses.
     * Used in conjunction with JUnit and Spring Test framework to validate API endpoints in the
     * AuthControllerTest class.
     *
     * This variable is automatically injected by Spring's dependency injection mechanism.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     *
     */
    @Mock
    private AuthService authService; // Use @Mock here

    /**
     * An instance of ObjectMapper used for JSON serialization and deserialization.
     * This object is typically employed in the testing of API endpoints where JSON
     * data needs to be converted to and from Java objects.
     *
     * This field is automatically injected by the Spring Framework using the
     * @Autowired annotation, ensuring that the ObjectMapper instance is properly
     * configured and managed within the application context.
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Sets up the test environment before each test case execution.
     *
     * This method initializes the mocks annotated with @Mock using the
     * MockitoAnnotations.openMocks method. This is necessary to properly
     * prepare the mocks for use in the test cases.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // still needed for @Mock fields
    }

    /**
     * Tests the login functionality of the authentication API with valid credentials.
     *
     * This method verifies that when a valid LoginDto containing correct username and
     * password is sent to the login endpoint, the response has an HTTP status of 200 (OK)
     * and the expected success message is returned in the response body. It mocks the
     * behavior of the authService to ensure the desired response is returned for a
     * valid login attempt.
     *
     * @throws Exception if an error occurs during the execution of the test
     */
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

    /**
     * Tests the login functionality of the authentication API with invalid credentials.
     *
     * This method verifies that when an invalid LoginDto containing incorrect username and
     * password is sent to the login endpoint, the response has an HTTP status of 401
     * (Unauthorized). It mocks the behavior of the authService to throw an exception
     * indicating invalid credentials for a failed login attempt.
     *
     * Test flow:
     * 1. Arrange: A LoginDto object is created with invalid credentials ("wronguser", "wrongpassword").
     * 2. Act: A POST request is sent to the login endpoint with the LoginDto payload.
     * 3. Assert: The response is verified to have a status code of 401 (Unauthorized).
     *
     * Throws:
     * Exception - if an error occurs during the request or response processing.
     */
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

    /**
     * Tests the login functionality when the username is missing in the request.
     *
     * This test validates that the authentication endpoint returns a Bad Request (HTTP 400)
     * status code when no username or email is provided in the login payload.
     *
     * Test flow:
     * 1. Arrange: A LoginDto object is created with only the password populated and no username.
     * 2. Act: A POST request is sent to the login endpoint with the LoginDto payload.
     * 3. Assert: The response is verified to have a status code of 400 (Bad Request).
     *
     * Throws:
     * Exception - if an error occurs during the request or response processing.
     */
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

    /**
     * Tests the login functionality of the authentication API when the password is missing in the login request.
     *
     * This method validates that the application returns a Bad Request (HTTP 400) status code
     * if a LoginDto is submitted without providing the password field.
     * The test ensures that the system appropriately enforces the requirement of a password
     * in order to proceed with the login process.
     *
     * Test flow:
     * 1. Arrange: A LoginDto object is created with the username or email populated, but without the password.
     * 2. Act: A POST request is sent to the login endpoint with the incomplete LoginDto payload.
     * 3. Assert: Verifies that the response returns an HTTP 400 Bad Request status code.
     *
     * Throws:
     * Exception - if an error occurs during the request or response processing.
     */
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

    /**
     * Tests the login functionality of the authentication API when the request body is empty.
     *
     * This method verifies that the application returns a Bad Request (HTTP 400) status code
     * when an empty JSON object is submitted as the login payload. This ensures that the
     * API correctly enforces the requirement for necessary fields in the request body.
     *
     * Test flow:
     * 1. Arrange: Prepare an empty JSON payload ({}).
     * 2. Act: Submit a POST request to the login endpoint with the empty payload.
     * 3. Assert: Verify that the response returns an HTTP 400 Bad Request status code.
     *
     * Throws:
     * Exception - if an error occurs during the request or response processing.
     */
    @Test
    @DisplayName("Test: Login with empty request body")
    public void testLoginWithEmptyRequestBody() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
    /**
     * Tests the register functionality of the authentication API with valid input.
     *
     * This method validates that when a proper RegisterDto containing valid username, email,
     * and password is sent, the response has an HTTP status of 200 (OK) and the expected
     * success message is returned in the response body.
     *
     * @throws Exception if an error occurs during the execution of the test
     */
    @Test
    @DisplayName("Test: Register with valid input")
    public void testRegisterWithValidInput() throws Exception {
        RegisterDto registerDto = new RegisterDto("testuser", "testuser@example.com", "password");

        when(authService.register(any(RegisterDto.class)))
                .thenReturn("User Registered Successfully!");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User Registered Successfully!"));
    }

    /**
     * Tests the register functionality of the authentication API with missing email.
     *
     * This test ensures that the API returns a Bad Request (HTTP 400) status code when
     * the email field is missing in the registration payload.
     *
     * @throws Exception if an error occurs during the request or response processing
     */
    @Test
    @DisplayName("Test: Register with missing email")
    public void testRegisterWithMissingEmail() throws Exception {
        RegisterDto registerDto = new RegisterDto("testuser", null, "password");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests the register functionality of the authentication API when the username is already taken.
     *
     * This method ensures that an appropriate error (HTTP 400 or conflict message) is returned
     * when attempting to register with a username that already exists.
     *
     * @throws Exception if an error occurs during the execution of the test
     */
    @Test
    @DisplayName("Test: Register when username is already taken")
    public void testRegisterWithDuplicateUsername() throws Exception {
        RegisterDto registerDto = new RegisterDto("duplicateUser", "testuser@example.com", "password");

        when(authService.register(any(RegisterDto.class)))
                .thenThrow(new RuntimeException("Username is already taken"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests the register functionality of the authentication API with an empty request body.
     *
     * This method validates that sending an empty JSON object results in a Bad Request
     * (HTTP 400) status code, ensuring the API enforces required fields.
     *
     * @throws Exception if an error occurs during the request or response process
     */
    @Test
    @DisplayName("Test: Register with empty request body")
    public void testRegisterWithEmptyRequestBody() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
