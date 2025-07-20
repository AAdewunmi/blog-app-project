package com.springapplication.blogappproject.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * RegisterDto is a Data Transfer Object used for transferring
 * user registration data within the application.
 * It encapsulates user information such as name, username, email, and password.
 * This class is typically used during user registration processes.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    /**
     * Represents the name of the user.
     * This field is used to store the full name of the user
     * during the registration process.
     */
    private String name;
    /**
     * Represents the username of a user.
     * This field is used to store a unique identifier for the user
     * that is typically chosen during the registration process.
     */
    private String username;
    /**
     * Represents the email address of the user.
     * This field stores the user's email, which is typically used for identification
     * or contact purposes within the user registration process.
     */
    private String email;
    /**
     * Represents the user's password used for authentication or registration purposes.
     * This field stores the password, which must be kept secure and confidential.
     */
    private String password;

    public RegisterDto(String username, String mail, String password) {
    }
}
