package com.springapplication.blogappproject.security;

import com.springapplication.blogappproject.service.CustomUserDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.HttpStatusEntryPoint;


/**
 * Configuration class for security settings in a Spring Boot application.
 *
 * This class leverages Spring Security to configure HTTP security settings, authentication,
 * and password encoding mechanisms. It also provides a configured ModelMapper for object mapping tasks.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * A private instance of {@link CustomUserDetailsService} used for handling user authentication
     * and authorization processes in the security configuration.
     *
     * This service is responsible for retrieving user details from the data source, such as the database,
     * to perform authentication and grant appropriate access based on user roles and permissions.
     *
     * It is utilized in configuring authentication providers and other security components within the
     * security configuration of the Spring Boot application.
     */
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Constructor for the SecurityConfig class.
     *
     * Initializes the security configuration with a custom implementation of
     * the {@link CustomUserDetailsService} for user authentication and authorization.
     *
     * @param customUserDetailsService an instance of {@link CustomUserDetailsService} used to load user details for authentication
     */
    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Creates and returns a bean of type {@link PasswordEncoder}.
     * The implementation used is {@link BCryptPasswordEncoder}, which provides a secure way to encode passwords
     * by employing the bcrypt hashing algorithm.
     *
     * @return a {@link PasswordEncoder} instance that utilizes bcrypt for password encoding
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the security filter chain for the application by specifying
     * rules for how HTTP requests are authorized, disabling CSRF protection,
     * enabling basic authentication, providing a custom authentication provider,
     * and defining an entry point for unauthorized access handling.
     *
     * @param http the {@link HttpSecurity} object used to configure the security settings for the application
     * @return a {@link SecurityFilterChain} object containing the configured security filters
     * @throws Exception if there is an error during the security configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .authenticationProvider(authenticationProvider())  // Add this line
            .exceptionHandling(exception ->
                exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            );

        return http.build();
    }

    /**
     * Configures and provides a customized {@link ModelMapper} bean for object mapping tasks.
     *
     * The {@link ModelMapper} is configured with the following settings:
     * - Null values are skipped during the mapping process.
     * - Ambiguity in the mapping process is ignored to prevent conflicts.
     * - Collection merging is disabled to ensure distinct mappings.
     * - Field matching is enabled, allowing mapping between fields with the same name.
     * - The access level for field matching is set to {@code PRIVATE}.
     *
     * @return a configured {@link ModelMapper} instance for use in the application
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setPropertyCondition(context -> context.getSource() != null)
                .setSkipNullEnabled(true)
                .setAmbiguityIgnored(true)
                .setCollectionsMergeEnabled(false)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return modelMapper;
    }

    /**
     * Configures and provides an instance of {@link AuthenticationManager} for managing
     * authentication in the Spring Security context.
     *
     * This method sets up a {@link DaoAuthenticationProvider} with the custom user details service
     * and password encoder. The authentication provider is then added to the {@link AuthenticationManagerBuilder}
     * for use in the application security configuration.
     *
     * @param http the {@link HttpSecurity} object used to configure web-based security for specific HTTP requests
     * @return an instance of {@link AuthenticationManager} configured with the specified {@link DaoAuthenticationProvider}
     * @throws Exception if an error occurs while building the {@link AuthenticationManager}
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(provider)
                .build();
    }

    /**
     * Configures and provides a {@link DaoAuthenticationProvider} bean for authentication.
     * This method sets up the authentication provider with a custom implementation of
     * {@link org.springframework.security.core.userdetails.UserDetailsService}
     * and a password encoder.
     *
     * @return a fully configured {@link DaoAuthenticationProvider} instance
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}