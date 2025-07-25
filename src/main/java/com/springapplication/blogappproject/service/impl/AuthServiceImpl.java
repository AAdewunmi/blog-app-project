package com.springapplication.blogappproject.service.impl;

import com.springapplication.blogappproject.entity.Role;
import com.springapplication.blogappproject.entity.User;
import com.springapplication.blogappproject.exception.BlogAPIException;
import com.springapplication.blogappproject.payload.LoginDto;
import com.springapplication.blogappproject.payload.RegisterDto;
import com.springapplication.blogappproject.repository.RoleRepository;
import com.springapplication.blogappproject.repository.UserRepository;
import com.springapplication.blogappproject.security.JwtTokenProvider;
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
     * Instance of AuthenticationManager used for handling user authentication
     * in the application. It is responsible for validating the provided
     * credentials (username and password) and managing authentication
     * within the security context.
     */
    private AuthenticationManager authenticationManager;
    /**
     * Repository used to manage User entities.
     * It provides methods for performing CRUD operations, checking the existence
     * of usernames and emails, and any custom query methods defined for user management.
     */
    private UserRepository userRepository;
    /**
     * Repository used for managing Role entities.
     * Provides methods to perform CRUD operations, find roles by name,
     * and check the existence of roles in the database.
     * Used in the AuthServiceImpl for role assignment and validation
     * during user registration.
     */
    private RoleRepository roleRepository;
    /**
     * Provides functionality for generating and validating JSON Web Tokens (JWTs).
     * This component is used within authentication and authorization processes to issue
     * tokens for authenticated users and verify their validity for secured operations.
     */

    private JwtTokenProvider jwtTokenProvider;
    /**
     * An instance of the PasswordEncoder used for secure encoding
     * of user passwords. The encoded passwords are stored in a
     * non-readable format for security purposes. This component
     * is utilized during user registration and password verification
     * processes to ensure the integrity and confidentiality of
     * sensitive user information.
     */
    private PasswordEncoder passwordEncoder;
    /**
     * Constructs an AuthServiceImpl instance using the provided dependencies.
     * These dependencies are used for user authentication, user and role management,
     * password encoding, and JWT token generation.
     *
     * @param authenticationManager the AuthenticationManager instance used
     *                              for user authentication and security context management.
     * @param userRepository the UserRepository instance used for performing
     *                       operations on User entities.
     * @param roleRepository the RoleRepository instance used for performing
     *                       operations on Role entities.
     * @param passwordEncoder the PasswordEncoder instance used for encoding
     *                        user passwords securely.
     * @param jwtTokenProvider the JwtTokenProvider instance used for generating
     *                         and validating JSON Web Tokens (JWTs).
     */
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
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
        String token = jwtTokenProvider.generateToken(authentication);
        return token;
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
