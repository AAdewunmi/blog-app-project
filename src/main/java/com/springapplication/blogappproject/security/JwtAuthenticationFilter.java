package com.springapplication.blogappproject.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A filter class for handling JWT-based authentication in a Spring security context.
 * This class intercepts HTTP requests to validate and parse JSON Web Tokens (JWTs)
 * and sets user authentication details into the SecurityContext if valid tokens are present.
 *
 * It extends an abstract filter designed to filter incoming HTTP requests in a Spring Security
 * environment and processes them before delegating to the next filter in the chain.
 *
 * This filter depends on:
 * - JwtTokenProvider: To validate and extract details from the JSON Web Token.
 * - UserDetailsService: To load user-specific details after extracting the username from the request's JWT.
 *
 * Designed to secure REST APIs by verifying token validity, retrieving user details,
 * and populating the SecurityContextHolder with authenticated user information.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    /**
     * Provides utility methods for handling JSON Web Tokens (JWT) in the Spring
     * Security context. This variable is used to validate, parse, and extract
     * details from JWTs during the authentication process.
     *
     * Acts as a dependency for the {@code JwtAuthenticationFilter} class and is
     * leveraged to validate incoming tokens and extract the username embedded
     * within them. It ensures secure token-based authentication by verifying the
     * token's integrity and expiration.
     */
    private JwtTokenProvider jwtTokenProvider;

    /**
     * A private instance of {@link UserDetailsService} used to load user-specific data.
     *
     * This service is responsible for retrieving user details based on the username
     * extracted from the JWT included in the request. It is utilized during authentication
     * to fetch the user's credentials and authorities.
     *
     * Acts as an integral part of the JWT-based authentication mechanism, ensuring
     * that user details are properly loaded and authenticated during request processing.
     */
    private UserDetailsService userDetailsService;

    /**
     * Constructs a new instance of the JwtAuthenticationFilter with the specified dependencies.
     *
     * @param jwtTokenProvider the provider used to validate and extract information from JSON Web Tokens (JWTs)
     * @param userDetailsService the service used to load user-specific details based on username
     */
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filters incoming requests by authenticating user credentials derived from a JSON Web Token (JWT).
     *
     * This method extracts the JWT from the request, validates the token, retrieves the associated
     * username, and loads the corresponding user details. If the token is valid and user details
     * are successfully retrieved, an authentication object is created and set in the security context.
     * Regardless of the authentication result, the filter chain continues with the request processing.
     *
     * @param request  the HTTP request received by the filter
     * @param response the HTTP response associated with the request
     * @param filterChain the filter chain to which the current filter belongs
     * @throws ServletException if an error occurs during the filter process
     * @throws IOException if an input or output error occurs while processing the filter
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/api/categories") || path.startsWith("/api/auth")) {
            filterChain.doFilter(request, response); // Skip JWT filter for these paths
            return;
        }
        String token = getTokenFromRequest(request);
        if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)){
            String username = jwtTokenProvider.getUsername(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts a JSON Web Token (JWT) from the "Authorization" header of the provided HTTP request.
     *
     * The method checks if the "Authorization" header contains a bearer token prefixed with "Bearer ".
     * If present, the token is returned without the "Bearer " prefix. If the header is absent or does
     * not follow the expected format, the method returns null.
     *
     * @param request the HTTP request containing the "Authorization" header
     * @return the extracted JWT if present and properly formatted, or null if not
     */
    private String getTokenFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
