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
}