package com.springapplication.blogappproject.security;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                ).httpBasic(Customizer.withDefaults()); // Enable HTTP Basic authentication
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
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("secret"))
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

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
