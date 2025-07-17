package com.springapplication.blogappproject.service;

import com.springapplication.blogappproject.entity.User;
import com.springapplication.blogappproject.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Custom implementation of the {@link UserDetailsService} interface.
 * This class is responsible for loading user-specific data, such as user details,
 * required for authentication and authorization in a Spring Security context.
 *
 * The user details are retrieved from the database using the {@link UserRepository}.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Repository interface for managing and accessing user data.
     * This variable serves as a dependency for performing CRUD operations
     * and custom queries on user-related data in the application.
     *
     * Used within the {@code CustomUserDetailsService} to retrieve user details
     * for authentication and authorization purposes in a Spring Security context.
     */
    private UserRepository userRepository;

    /**
     * Constructs a new instance of CustomUserDetailsService.
     *
     * @param userRepository the UserRepository used for accessing user-related data from the database
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Locates the user details for authentication using the provided username or email.
     * Attempts to retrieve the user from the database by either username or email.
     * If the user is found, their associated roles are retrieved and mapped to
     * GrantedAuthority objects to build a UserDetails object, which is required
     * for Spring Security authentication.
     *
     * @param usernameOrEmail the username or email address used to identify the user
     * @return a UserDetails object containing the user's credentials and authorities
     * @throws UsernameNotFoundException if no user is found with the provided username or email
     */
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email: "+ usernameOrEmail));

        Set<GrantedAuthority> authorities = user
                .getRoles()
                .stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                authorities);
    }

}
