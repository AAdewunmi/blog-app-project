package com.springapplication.blogappproject.utils;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for {@code PasswordGeneratorEncoder} functionality.
 * This class is used to validate that passwords when encoded by
 * {@code BCryptPasswordEncoder} are properly hashed and can be
 * verified against the original raw passwords.
 */
class PasswordGeneratorEncoderTest {

    @Test
    void testPasswordEncodingForAdmin() {
        String password = "admin";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String encodedPassword = passwordEncoder.encode(password);

        assertNotNull(encodedPassword, "Encoded password should not be null");
        assertTrue(passwordEncoder.matches(password, encodedPassword),
                "Encoded password should match the raw password when verified");
    }

}