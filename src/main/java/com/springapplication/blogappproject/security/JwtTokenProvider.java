package com.springapplication.blogappproject.security;

import com.springapplication.blogappproject.exception.BlogAPIException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

/**
 * This class provides utility methods for generating JSON Web Tokens (JWT) in a Spring Boot application.
 * It is annotated as a Spring {@code @Component} to be automatically detected as a Spring-managed bean.
 * The configuration for JWT secret and expiration time is injected from application properties.
 */
@Component
public class JwtTokenProvider {

    /**
     * The secret key used to sign and verify JSON Web Tokens (JWT) in the application.
     *
     * This value is injected from the application properties using the {@code @Value} annotation.
     * The property key is specified as {@code app.jwt-secret}, which is expected to be defined
     * in the application's configuration file (e.g., application.properties or application.yml).
     *
     * This key is critical for ensuring the integrity and authenticity of JWTs by enabling
     * the application to create and validate tokens securely.
     */
    @Value("${app.jwt-secret}")
    private String jwtSecret;

    /**
     * Represents the expiration duration in milliseconds for JSON Web Tokens (JWT).
     *
     * This value is injected from the application properties using the key `app-jwt-expiration-milliseconds`.
     * It determines the validity period of a generated JWT after which the token will expire and become invalid.
     *
     * The default duration is typically configured in the application's properties file,
     * and it is used to set the expiration date during the JWT generation process.
     */
    @Value("${app-jwt-expiration-milliseconds}")
    private long jwtExpirationDate;


    /**
     * Generates a JSON Web Token (JWT) for the authenticated user.
     *
     * This method creates a JWT that includes the user's username as the subject,
     * the current timestamp as the issue time, and a predefined expiration time.
     * The token is signed using a secret key.
     *
     * @param authentication the authentication object containing details of the currently authenticated user
     * @return a JWT string representing the authenticated user's session
     */
    public String generateToken(Authentication authentication){

        String username = authentication.getName();

        Date currentDate = new Date();

        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(expireDate)
                .signWith(key())
                .compact();

        return token;
    }

}
