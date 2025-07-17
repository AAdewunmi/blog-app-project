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

    /**
     * Tests the password encoding functionality for the raw password "admin".
     * This method ensures that the {@code BCryptPasswordEncoder} properly encodes the raw password
     * and that the encoded password can be validated against the original raw password.
     *
     * The test performs the following checks:
     * - Verifies that the encoded password is not null.
     * - Asserts that the raw password matches the encoded password when validated using the encoder.
     */
    @Test
    void testPasswordEncodingForAdmin() {
        String password = "admin";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String encodedPassword = passwordEncoder.encode(password);

        assertNotNull(encodedPassword, "Encoded password should not be null");
        assertTrue(passwordEncoder.matches(password, encodedPassword),
                "Encoded password should match the raw password when verified");
    }

    /**
     * Tests the password encoding functionality for the raw password "ramesh".
     * This method validates that the {@code BCryptPasswordEncoder} correctly encodes the raw password
     * and ensures that the encoded password matches the original raw password upon verification.
     *
     * The test includes the following checks:
     * - Confirms that the encoded password is not null.
     * - Asserts that the raw password matches the encoded password when validated with the encoder.
     */
    @Test
    void testPasswordEncodingForRamesh() {
        String password = "ramesh";
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String encodedPassword = passwordEncoder.encode(password);

        assertNotNull(encodedPassword, "Encoded password should not be null");
        assertTrue(passwordEncoder.matches(password, encodedPassword),
                "Encoded password should match the raw password when verified");
    }

}