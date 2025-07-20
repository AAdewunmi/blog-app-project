package com.springapplication.blogappproject.service.impl;

import com.springapplication.blogappproject.entity.Role;
import com.springapplication.blogappproject.entity.User;
import com.springapplication.blogappproject.exception.BlogAPIException;
import com.springapplication.blogappproject.payload.LoginDto;
import com.springapplication.blogappproject.payload.RegisterDto;
import com.springapplication.blogappproject.repository.RoleRepository;
import com.springapplication.blogappproject.repository.UserRepository;
import com.springapplication.blogappproject.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of the AuthService interface for user authentication.
 * This service provides functionality for authenticating users
 * and returning a JWT token upon successful login.
 */
@Service
public class AuthServiceImpl implements AuthService {

    /**
     * Manages user authentication within the system.
     * This variable is used to authenticate user credentials
     * and establish security contexts for further operations.
     */
    private AuthenticationManager authenticationManager;
    /**
     * Repository interface used for managing User entities in the application.
     * Provides methods to perform CRUD operations on users, as well as
     * custom queries for retrieving, validating, and checking the existence
     * of users by specific criteria such as username or email.
     */
    private UserRepository userRepository;
    /**
     * Repository used to manage Role entities.
     * It provides methods to perform CRUD operations and retrieve
     * specific roles based on custom query methods like finding roles
     * by their names or checking their existence.
     */
    private RoleRepository roleRepository;
    /**
     * Encodes passwords using a specified password encoding algorithm.
     * This variable provides functionality for securely hashing and
     * validating password data to ensure user authentication processes
     * adhere to security best practices.
     */
    private PasswordEncoder passwordEncoder;
    /**
     * Constructs an instance of AuthServiceImpl with dependencies for handling
     * authentication, user management, role management, and password encoding.
     *
     * @param authenticationManager the AuthenticationManager responsible for
     *                              authenticating user credentials.
     * @param userRepository         the UserRepository for managing user entity
     *                               interactions such as retrieval and storage.
     * @param roleRepository         the RoleRepository for managing and retrieving
     *                               role-related data.
     * @param passwordEncoder        the PasswordEncoder for encoding and validating
     *                               user passwords securely.
     */
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    /**
     * Authenticates a user based on the provided login credentials
     * (username or email and password). If the authentication is
     * successful, it returns a JWT token.
     *
     * @param loginDto the data transfer object containing the user's
     *                 login credentials, including username or email
     *                 and password.
     * @return a JWT token as a string if the authentication is
     *         successful.
     */
    @Override
    public String login(LoginDto loginDto) {
        Authentication authentication =
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsernameOrEmail(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "User Logged-In Successfully!";
    }

    /**
     * Registers a new user based on the provided registration details.
     * Validates that the username and email are unique, sets the user's role,
     * hashes the password, and saves the user to the repository.
     *
     * @param registerDto the data transfer object containing the user's registration details
     *                    such as name, username, email, and password.
     * @return a message indicating successful registration of the user.
     * @throws BlogAPIException if the username or email already exists, or if the user role is not found.
     */
    @Override
    public String register(RegisterDto registerDto) {

        if(userRepository.existsByUsername(registerDto.getUsername())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
        }

        if(userRepository.existsByEmail(registerDto.getEmail())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email is already exists!.");
        }

        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(() ->
                new BlogAPIException(HttpStatus.BAD_REQUEST, "Role not found!"));
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);

        return "User registered successfully!.";
    }

}
