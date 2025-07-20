package com.springapplication.blogappproject.service;

import com.springapplication.blogappproject.payload.LoginDto;
import com.springapplication.blogappproject.payload.RegisterDto;

/**
 * Service interface for managing user authentication.
 * Provides methods related to authentication functionality such as logging in.
 */
public interface AuthService  {

    /**
     * Authenticates a user using the provided login credentials.
     * Accepts a LoginDto object containing the username or email and password,
     * and returns a JWT token upon successful authentication.
     *
     * @param loginDto the data transfer object containing user credentials for login
     * @return a string representing the JWT token if authentication is successful
     */
    String login(LoginDto loginDto);

    /**
     * Registers a new user with the provided registration details.
     * Accepts a RegisterDto object containing user information such as name, username, email, and password.
     *
     * @param registerDto the data transfer object containing user registration details
     * @return a string indicating the result of the registration process
     */
    String register(RegisterDto registerDto);

}
