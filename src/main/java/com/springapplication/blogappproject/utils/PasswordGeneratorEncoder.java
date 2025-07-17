package com.springapplication.blogappproject.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * PasswordGeneratorEncoder is a utility class used to generate encoded passwords
 * using the BCryptPasswordEncoder implementation from the Spring Security framework.
 *
 * This class provides an entry point to encode hardcoded plaintext passwords
 * and output their encoded representations to the console.
 *
 * BCryptPasswordEncoder is leveraged to apply hashing and salting to passwords,
 * making them secure for storage and resilient against brute-force attacks.
 *
 * Usage of this class is typically for testing or development purposes
 * to generate hashed passwords that can be used in application configuration or databases.
 */
public class PasswordGeneratorEncoder {
    /**
     * The main method is the entry point to the PasswordGeneratorEncoder application.
     * It demonstrates the encoding of plaintext passwords using {@link BCryptPasswordEncoder}.
     * Encoded passwords are printed to the console.
     * This method is typically used for testing and generating secure password hashes.
     *
     * @param args command-line arguments passed to the program; not used in this implementation.
     */
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("admin"));
        // $2a$10$pxdfgstxXvvlgvYHOzdZpOYnbvTkiNT0sc6Mrsm0O9W.MiUxYKomq
        System.out.println(passwordEncoder.encode("ramesh"));
        //$2a$10$Avu1MsR62lnzO2aHx5NPSO0eu8klfkJwgu0QNRiJJOb0GrHMo0LMa
    }
}
