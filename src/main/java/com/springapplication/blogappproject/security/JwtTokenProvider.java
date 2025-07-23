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

    /**
     * Generates and returns a cryptographic key used for signing and verifying JWTs.
     *
     * This method decodes the JWT secret, which is encoded in Base64 format,
     * and creates an HMAC-SHA key suitable for use with JSON Web Token operations.
     * The key is critical for ensuring secure token creation and validation.
     *
     * @return a {@link Key} object representing the HMAC-SHA cryptographic key for JWT signing
     */
    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    /**
     * Extracts and returns the username from the provided JSON Web Token (JWT).
     *
     * This method parses the JWT, validates its signature using the secret key,
     * and retrieves the "subject" field from the token's payload, which represents the username.
     *
     * @param token the JSON Web Token (JWT) string from which the username is to be extracted
     * @return the username contained within the "subject" field of the token's payload
     * @throws io.jsonwebtoken.JwtException if the token is invalid, expired, or fails signature validation
     */
    public String getUsername(String token){

        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Validates the provided JSON Web Token (JWT) for authenticity, expiration, and format.
     *
     * This method parses the token using the configured cryptographic key and checks
     * its signature validity. If the token is valid, it returns true. Otherwise,
     * it throws an exception specifying the reason for invalidity.
     *
     * @param token the JSON Web Token (JWT) string to validate
     * @return true if the token is valid and properly formatted
     * @throws BlogAPIException if the token is invalid, expired, unsupported,
     *         or contains null/empty claims
     */
    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .verifyWith((SecretKey) key())
                    .build()
                    .parse(token);
            return true;
        }catch (MalformedJwtException malformedJwtException){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT Token");
        }catch (ExpiredJwtException expiredJwtException){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        }catch (UnsupportedJwtException unsupportedJwtException){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        }catch (IllegalArgumentException illegalArgumentException){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Jwt claims string is null or empty");
        }
    }

}
