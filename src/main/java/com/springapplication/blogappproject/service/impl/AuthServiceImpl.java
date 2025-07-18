package com.springapplication.blogappproject.service.impl;

import com.springapplication.blogappproject.payload.LoginDto;
import com.springapplication.blogappproject.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
     * Constructs a new AuthServiceImpl with the provided AuthenticationManager.
     *
     * @param authenticationManager the AuthenticationManager used for authenticating users
     */
    public AuthServiceImpl(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
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

}
