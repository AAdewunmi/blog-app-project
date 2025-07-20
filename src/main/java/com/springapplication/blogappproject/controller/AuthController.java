package com.springapplication.blogappproject.controller;

import com.springapplication.blogappproject.payload.LoginDto;
import com.springapplication.blogappproject.payload.RegisterDto;
import com.springapplication.blogappproject.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController is a REST controller responsible for handling
 * user authentication requests.
 * It provides endpoints for user login functionality.
 */
@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    /**
     * A dependency representing the authentication service used by the AuthController.
     * This service provides methods to handle user authentication, such as logging in.
     * It is injected into the controller to delegate the authentication logic.
     */
    private AuthService authService;

    /**
     * Constructs an instance of AuthController with the provided AuthService.
     * The AuthService is used to handle business logic related to user authentication.
     *
     * @param authService the service handling user authentication operations
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Handles user authentication by accepting login credentials in the form of a LoginDto
     * and returning a response containing a JWT token if successful.
     *
     * @param loginDto the data transfer object containing user credentials, such as username or email and password
     * @return a ResponseEntity containing the JWT token as a string upon successful authentication
     */
    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        String response = authService.login(loginDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Handles user registration by accepting user details in the form of a RegisterDto
     * and returning a response indicating the result of the registration process.
     *
     * @param registerDto the data transfer object containing user registration details such as name, username, email, and password
     * @return a ResponseEntity containing a message indicating the result of the registration process
     */
    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        String response = authService.register(registerDto);
        return ResponseEntity.ok(response);
    }
}
