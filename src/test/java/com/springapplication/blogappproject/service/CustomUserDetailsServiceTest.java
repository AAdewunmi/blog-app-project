package com.springapplication.blogappproject.service;

import com.springapplication.blogappproject.entity.Role;
import com.springapplication.blogappproject.entity.User;
import com.springapplication.blogappproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the CustomUserDetailsService class.
 * This class contains test methods for validating the functionality of the
 * loadUserByUsername method in CustomUserDetailsService.
 *
 * It ensures that the service correctly retrieves user details and constructs
 * a UserDetails object using user data retrieved from the UserRepository.
 */
class CustomUserDetailsServiceTest {

    /**
     * Tests the functionality of the CustomUserDetailsService's loadUserByUsername method
     * when a user is found in the repository.
     *
     * This test ensures that:
     * 1. The repository method findByUsernameOrEmail is invoked with the correct parameters.
     * 2. A non-null UserDetails object is returned.
     * 3. The returned UserDetails object has the expected username, password, and authorities.
     * 4. The authorities in UserDetails are correctly mapped from the user's roles.
     *
     * Test Steps:
     * - Mock the UserRepository to return a valid User object when called with a specific
     *   username or email.
     * - Initialize the CustomUserDetailsService with the mocked UserRepository.
     * - Call the loadUserByUsername method with a valid username or email.
     * - Verify that the returned UserDetails object matches the expected data.
     * - Ensure the repository method is invoked exactly once.
     */
    @Test
    void testLoadUserByUsername_UserFound() {
        // Arrange
        UserRepository mockUserRepository = mock(UserRepository.class);
        CustomUserDetailsService service = new CustomUserDetailsService(mockUserRepository);

        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password123");
        Role role = new Role();
        role.setName("ROLE_USER");
        mockUser.setRoles(Set.of(role));

        when(mockUserRepository.findByUsernameOrEmail("test@example.com", "test@example.com"))
                .thenReturn(Optional.of(mockUser));

        // Act
        UserDetails userDetails = service.loadUserByUsername("test@example.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals(mockUser.getEmail(), userDetails.getUsername());
        assertEquals(mockUser.getPassword(), userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));

        verify(mockUserRepository, times(1))
                .findByUsernameOrEmail("test@example.com", "test@example.com");
    }

    /**
     * Tests the functionality of the CustomUserDetailsService's loadUserByUsername method
     * when a user is not found in the repository.
     *
     * This test ensures that:
     * 1. The repository method findByUsernameOrEmail is invoked with the correct parameters.
     * 2. A UsernameNotFoundException is thrown when no user exists with the specified username or email.
     * 3. The exception message correctly indicates that the user was not found.
     *
     * Test Steps:
     * - Mock the UserRepository to return an empty Optional when called with a non-existent username or email.
     * - Initialize the CustomUserDetailsService with the mocked UserRepository.
     * - Call the loadUserByUsername method with a username or email that does not exist in the system.
     * - Assert that the expected exception is thrown.
     * - Verify that the repository method is invoked exactly once with the correct arguments.
     */
    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        UserRepository mockUserRepository = mock(UserRepository.class);
        CustomUserDetailsService service = new CustomUserDetailsService(mockUserRepository);

        when(mockUserRepository.findByUsernameOrEmail("nonexistent@example.com", "nonexistent@example.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserByUsername("nonexistent@example.com")
        );

        assertEquals("User not found with username or email: nonexistent@example.com", exception.getMessage());

        verify(mockUserRepository, times(1))
                .findByUsernameOrEmail("nonexistent@example.com", "nonexistent@example.com");
    }

    /**
     * Tests the behavior of the `CustomUserDetailsService` class's `loadUserByUsername` method
     * when a user exists in the repository but does not have any assigned roles.
     *
     * This test verifies that:
     * 1. The repository method `findByUsernameOrEmail` is invoked with the correct parameters.
     * 2. A non-null `UserDetails` object is returned.
     * 3. The username and password in the returned `UserDetails` object match the corresponding fields of the user.
     * 4. The `UserDetails` object contains an empty list of authorities since the user has no roles.
     *
     * Test Steps:
     * - Mock the `UserRepository` to return a `User` entity with no roles assigned.
     * - Instantiate the `CustomUserDetailsService` with the mocked `UserRepository`.
     * - Invoke the `loadUserByUsername` method with a valid username or email.
     * - Assert that the returned `UserDetails` object is not null.
     * - Assert that the username and password of the `UserDetails` object match the mocked user's email and password.
     * - Assert that the authorities of the `UserDetails` object are empty.
     * - Verify that the `findByUsernameOrEmail` method of the repository is called exactly once with the correct arguments.
     */
    @Test
    void testLoadUserByUsername_UserWithNoRoles() {
        // Arrange
        UserRepository mockUserRepository = mock(UserRepository.class);
        CustomUserDetailsService service = new CustomUserDetailsService(mockUserRepository);

        User mockUser = new User();
        mockUser.setEmail("noroles@example.com");
        mockUser.setPassword("password123");
        mockUser.setRoles(Collections.emptySet());

        when(mockUserRepository.findByUsernameOrEmail("noroles@example.com", "noroles@example.com"))
                .thenReturn(Optional.of(mockUser));

        // Act
        UserDetails userDetails = service.loadUserByUsername("noroles@example.com");

        // Assert
        assertNotNull(userDetails);
        assertEquals(mockUser.getEmail(), userDetails.getUsername());
        assertEquals(mockUser.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());

        verify(mockUserRepository, times(1))
                .findByUsernameOrEmail("noroles@example.com", "noroles@example.com");
    }
}