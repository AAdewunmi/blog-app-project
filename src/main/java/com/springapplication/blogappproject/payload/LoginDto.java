package com.springapplication.blogappproject.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * LoginDto is a Data Transfer Object used for user authentication.
 * It encapsulates the user's credentials required for login,
 * including a username or email and a password.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    /**
     * Represents a field used for user login credentials.
     * This field can store either a username or an email address,
     * depending on the authentication method chosen.
     */
    private String usernameOrEmail;
    /**
     * Represents the user's password used for authentication.
     * This field stores the password provided during login
     * and is a required credential for user verification.
     */
    private String password;
}
