package com.springapplication.blogappproject.service.impl;

import com.springapplication.blogappproject.entity.Role;
import com.springapplication.blogappproject.entity.User;
import com.springapplication.blogappproject.exception.BlogAPIException;
import com.springapplication.blogappproject.payload.LoginDto;
import com.springapplication.blogappproject.payload.RegisterDto;
import com.springapplication.blogappproject.repository.RoleRepository;
import com.springapplication.blogappproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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
          * Mocked instance of {@code UserRepository} used for testing purposes.
          * This mock replaces the actual {@code UserRepository} implementation
          * in test cases, allowing simulation of its behavior without interacting
          * with the actual data source.
          *
          * The mocked repository is primarily used to test and verify the behavior
          * of the {@code AuthServiceImpl} methods, such as {@code register} and {@code login},
          * in scenarios that involve user data operations like retrieval, checking existence,
          * or saving users.
          *
          * This allows comprehensive testing of application logic while isolating it
          * from external dependencies.
          */
         @Mock
         private UserRepository userRepository;

         /**
          * Mocked instance of {@code RoleRepository} used for testing purposes in {@code AuthServiceImplTest}.
          *
          * This mock is utilized to simulate interactions with the database for role-related operations,
          * such as querying roles or verifying their existence, without the need for an actual database connection.
          *
          * The {@code RoleRepository} provides methods for retrieving and interacting with role entities,
          * including finding roles by name and checking their existence.
          */
         @Mock
         private RoleRepository roleRepository;

         /**
          * Mocked instance of the {@code PasswordEncoder} interface used for testing purposes in the
          * {@code AuthServiceImplTest} class.
          *
          * This field is utilized to simulate password encoding functionality during unit tests, ensuring
          * that password encoding logic can be verified without requiring an actual implementation
          * or external dependencies.
          *
          * Common use cases include:
          * - Verifying that passwords are encoded correctly before persisting them.
          * - Simulating behaviors or conditions for password encoding during tests.
          *
          * The behavior of this mock object can be customized using mocking frameworks, allowing controlled
          * responses to method calls as needed for specific test scenarios.
          */
         @Mock
         private PasswordEncoder passwordEncoder;

         /**
          * Tests the {@code register} method of {@code AuthServiceImpl} to ensure
          * that a valid user registration succeeds and returns the correct success message.
          *
          * This test verifies the following:
          * - A new user with a unique username and email can be registered.
          * - The correct role is assigned to the user during registration.
          * - The password is encoded securely before storing.
          * - The user is saved successfully in the repository.
          * - The returned message accurately reflects the success of the registration process.
          *
          * Preconditions:
          * - The username and email provided in the {@code RegisterDto} do not already exist in the system.
          * - The "ROLE_USER" role exists in the role repository.
          *
          * Expected behavior:
          * - The system should create a new user with the given details, assign the "ROLE_USER" role,
          *   encode the provided password, and persist the user in the database.
          * - The method should return the success message "User registered successfully!.".
          */
         @Test
         public void testRegister_ValidUserDetails_ReturnsSuccessMessage() {
        // Arrange
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("testuser");
        registerDto.setEmail("testuser@example.com");
        registerDto.setPassword("password123");
        registerDto.setName("Test User");

        Role mockRole = new Role();
        mockRole.setName("ROLE_USER");

        Mockito.when(userRepository.existsByUsername("testuser")).thenReturn(false);
        Mockito.when(userRepository.existsByEmail("testuser@example.com")).thenReturn(false);
        Mockito.when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(mockRole));
        Mockito.when(passwordEncoder.encode("password123")).thenReturn("encryptedPassword");

        // Act
        String result = authService.register(registerDto);

        // Assert
        assertEquals("User registered successfully!.", result);
        Mockito.verify(userRepository).save(Mockito.any(User.class));
         }

         /**
          * Tests the {@code register} method of {@code AuthServiceImpl} to ensure
          * that an attempt to register a user with an existing username throws a {@code BlogAPIException}.
          *
          * This test verifies the following:
          * - The system checks whether the username already exists in the repository.
          * - An exception is thrown if the username is already registered in the system.
          * - The exception message matches the expected error message indicating the username's existence.
          *
          * Preconditions:
          * - A user with the username "testuser" already exists in the system.
          *
          * Expected behavior:
          * - The method should throw a {@code BlogAPIException} with the message "Username is already exists!.".
          * - The exception handling should capture this scenario, ensuring the registration process does not proceed.
          */
         @Test
         public void testRegister_ExistingUsername_ThrowsException() {
        // Arrange
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("testuser");
        registerDto.setEmail("testuser@example.com");
        registerDto.setPassword("password123");
        registerDto.setName("Test User");

        Mockito.when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // Act & Assert
        try {
            authService.register(registerDto);
        } catch (BlogAPIException ex) {
            assertEquals("Username is already exists!.", ex.getMessage());
        }
         }

         /**
          * Tests the {@code register} method of {@code AuthServiceImpl} to ensure
          * that an attempt to register a user with an existing email throws a {@code BlogAPIException}.
          *
          * This test verifies the following:
          * - The system checks whether the email address already exists in the repository.
          * - An exception is thrown if the email is already registered in the system.
          * - The exception message matches the expected error message indicating the email's existence.
          *
          * Preconditions:
          * - A user with the email "testuser@example.com" already exists in the system.
          *
          * Expected behavior:
          * - The method should throw a {@code BlogAPIException} with the message "Email is already exists!.".
          * - The exception handling should capture this scenario, ensuring the registration process does not proceed.
          */
         @Test
         public void testRegister_ExistingEmail_ThrowsException() {
        // Arrange
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("testuser");
        registerDto.setEmail("testuser@example.com");
        registerDto.setPassword("password123");
        registerDto.setName("Test User");

        Mockito.when(userRepository.existsByEmail("testuser@example.com")).thenReturn(true);

        // Act & Assert
        try {
            authService.register(registerDto);
        } catch (BlogAPIException ex) {
            assertEquals("Email is already exists!.", ex.getMessage());
        }
         }

         /**
          * Tests the {@code register} method of {@code AuthServiceImpl} to ensure
          * that an attempt to register a user fails if the required role is missing in the system.
          *
          * This test verifies the following:
          * - The system attempts to fetch the "ROLE_USER" role from the repository.
          * - An exception of type {@code BlogAPIException} is thrown if the role is not found.
          * - The exception message matches the expected error message indicating the missing role.
          *
          * Preconditions:
          * - The username and email provided in the {@code RegisterDto} do not already exist in the system.
          * - The "ROLE_USER" role does not exist in the role repository.
          *
          * Expected behavior:
          * - The method should throw a {@code BlogAPIException} with the message "Role not found!".
          * - The registration process should not proceed if the required role is unavailable.
          */
         @Test
         public void testRegister_MissingRole_ThrowsException() {
        // Arrange
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("testuser");
        registerDto.setEmail("testuser@example.com");
        registerDto.setPassword("password123");
        registerDto.setName("Test User");

        Mockito.when(userRepository.existsByUsername("testuser")).thenReturn(false);
        Mockito.when(userRepository.existsByEmail("testuser@example.com")).thenReturn(false);
        Mockito.when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

        // Act & Assert
        try {
            authService.register(registerDto);
        } catch (BlogAPIException ex) {
            assertEquals("Role not found!", ex.getMessage());
        }
         }

    /**
     * Tests the {@code login} method of {@code AuthServiceImpl} to ensure
     * that valid user credentials result in successful authentication and
     * return the appropriate success message.
     *
     * This test verifies the following:
     * - A {@code LoginDto} containing valid credentials is created and
     *   passed correctly to the authentication process.
     * - The {@code AuthenticationManager}'s {@code authenticate} method
     *   is invoked with a {@code UsernamePasswordAuthenticationToken}.
     * - Upon successful authentication, the expected success message
     *   "User Logged-In Successfully!" is returned.
     *
     * Preconditions:
     * - The credentials provided in the {@code LoginDto} are valid and
     *   correspond to an existing user in the system.
     *
     * Expected behavior:
     * - The authentication process succeeds without exceptions.
     * - The method returns the success message "User Logged-In Successfully!".
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

    /**
     * Tests the {@code login} method of {@code AuthServiceImpl} to ensure
     * that invalid user credentials result in an exception being thrown.
     *
     * This test verifies the following:
     * - A {@code LoginDto} containing incorrect credentials is correctly
     *   passed to the authentication process.
     * - The {@code AuthenticationManager}'s {@code authenticate} method
     *   is invoked with a {@code UsernamePasswordAuthenticationToken}.
     * - When the authentication fails, a {@code RuntimeException} with
     *   the message "Invalid credentials" is thrown.
     */
    @Test
    public void testLogin_InvalidCredentials_ThrowsException() {
        // Arrange
        LoginDto loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("testuser");
        loginDto.setPassword("wrongpassword");

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // Act & Assert
        try {
            authService.login(loginDto);
        } catch (RuntimeException ex) {
            assertEquals("Invalid credentials", ex.getMessage());
        }

        Mockito.verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}