package com.springapplication.blogappproject.security;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashSet;
import java.util.Set;

/**
 * Security configuration class for the Blog Application Project.
 * This class configures security settings, including user authentication and CSRF protection.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * Configures the security filter chain for the application.
     * This method sets up HTTP security, including CSRF protection and basic authentication.
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Explicitly configure CSRF protection
                .csrf(AbstractHttpConfigurer::disable) // Only disable for non-browser clients like Postman
                .authorizeHttpRequests(auth -> auth
                        .anyRequest()
                        .authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Provides an in-memory user details service with a single user.
     * This method creates a user with username "admin", password "secret", and role "USER".
     *
     * @return the UserDetailsService containing the user
     */
    @Bean
    public UserDetailsService users() {
        UserDetails user = User
                .withUsername("admin")
                .password("{noop}secret")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

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