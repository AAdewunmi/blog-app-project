package com.springapplication.blogappproject.security;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.http.HttpMethod;

import java.util.HashSet;
import java.util.Set;

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
     * Represents a {@link UserDetailsService} implementation that is used for retrieving
     * user-specific data during authentication and authorization processes.
     *
     * This variable is utilized to manage user details, including loading user-specific
     * information from a data source for authentication purposes. It is essential in
     * the Spring Security framework to support various functionalities such as custom
     * user management and integration with external user details sources.
     */
    private UserDetailsService userDetailsService;

    /**
     * Default constructor for the SecurityConfig class.
     *
     * Constructs a SecurityConfig instance with no specific initialization.
     * This constructor can be used when the application does not require
     * a custom UserDetailsService or other specific configurations at the time
     * of SecurityConfig instantiation.
     */
    public SecurityConfig() {
    }

    /**
     * Constructs a SecurityConfig instance using the provided UserDetailsService.
     *
     * @param userDetailsService the UserDetailsService used for loading user-specific data
     */
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Provides an {@link AuthenticationManager} bean configured with the application's authentication settings.
     *
     * This method retrieves the {@link AuthenticationManager} instance from the provided
     * {@link AuthenticationConfiguration}, which is responsible for managing authentication
     * processes within the application.
     *
     * @param configuration the {@link AuthenticationConfiguration} containing the authentication settings
     *                       and required configurations for the application
     * @return the {@link AuthenticationManager} instance to be used for authentication processes
     * @throws Exception if an error occurs while retrieving the {@link AuthenticationManager}
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
    /**
     * Configures a filter chain for HTTP security settings in a Spring Boot application.
     * This method enables authentication for all HTTP requests and provides a default login form.
     *
     * @param http the {@code HttpSecurity} object to configure security-specific settings
     * @return the configured {@code SecurityFilterChain} instance
     * @throws Exception if an error occurs while building the filter chain
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection for simplicity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/**").permitAll()  // Public GET access
                        .anyRequest().authenticated()                            // All others require auth
                )
                .httpBasic(Customizer.withDefaults()); // Enable HTTP Basic authentication
                //.formLogin(form -> form.permitAll());  // Enable default Spring Boot login form
        return http.build();
    }
    /**
     * Creates and provides a PasswordEncoder bean configured with BCrypt hashing algorithm.
     * This is used to securely encrypt and verify passwords in the application.
     *
     * @return a PasswordEncoder instance configured with the BCrypt hashing algorithm
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides a {@link UserDetailsService} implementation with predefined user details.
     *
     * This method creates an in-memory user with the following attributes:
     * - Username: "admin"
     * - Password: Encrypted value of "secret" using the provided {@link PasswordEncoder}
     * - Roles: "USER", "ADMIN"
     *
     * The {@link InMemoryUserDetailsManager} is returned as the implementation for managing user details.
     *
     * @param passwordEncoder the {@link PasswordEncoder} used for encoding the user's password
     * @return a {@link UserDetailsService} instance containing the in-memory defined user
     */
//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//        UserDetails user = User.builder()
//                .username("admin")
//                .password(passwordEncoder.encode("secret"))
//                .roles("USER", "ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }

    /**
     * Configures and provides a {@link ModelMapper} bean for object mapping tasks.
     *
     * The provided {@link ModelMapper} instance is configured with the following:
     * - Skips null values during mapping to ensure only non-null source values are mapped.
     * - Ignores ambiguity in the mapping process.
     * - Disables collection merging to prevent automatic merging of collections during mapping.
     * - Enables field matching with {@code PRIVATE} access level for accessing private fields.
     * - Includes a custom {@link AbstractConverter} to handle deep copying of {@link Set} objects.
     *
     * @return a fully configured {@link ModelMapper} instance for mapping between objects.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setPropertyCondition(context -> context.getSource() != null)
                .setSkipNullEnabled(true)
                .setAmbiguityIgnored(true)
                .setCollectionsMergeEnabled(false) // Disable collections merging
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        modelMapper.addConverter(new AbstractConverter<Set<?>, Set<?>>() {
            @Override
            protected Set<?> convert(Set<?> source) {
                return source == null ? null : new HashSet<>(source);
            }
        });
        return modelMapper;
    }

}
