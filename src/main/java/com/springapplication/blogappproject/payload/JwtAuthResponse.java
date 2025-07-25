package com.springapplication.blogappproject.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JwtAuthResponse is a representation of the authentication response
 * returned after a successful login or token generation.
 * It contains the access token and its type, typically used
 * for securing later API requests.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponse {
    /**
     * Represents the access token generated after successful authentication.
     * This token is used for securing later API requests and ensuring
     * that they are performed by an authenticated user.
     */
    private String accessToken;
    /**
     * Represents the type of the token used in authentication responses.
     * This field is typically set to represent a specific token type,
     * such as "Bearer", which is commonly used in authorization headers
     * to indicate the type of token being passed.
     */
    private String tokenType = "Bearer";

    public JwtAuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
