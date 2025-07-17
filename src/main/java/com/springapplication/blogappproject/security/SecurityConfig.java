package com.springapplication.blogappproject.security;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
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
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers("/api/auth/**").permitAll();
                auth.anyRequest().authenticated();
            })
            .exceptionHandling(exception -> 
                exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            );
        
        return http.build();
    }

    /**
     * Defines a {@link ModelMapper} bean with custom configuration settings for object mapping tasks.
     *
     * The configured {@link ModelMapper}:
     * - Ignores null source values during mapping.
     * - Skips ambiguous mappings by ignoring them.
     * - Disables automatic merging of collections during mapping.
     * - Enables field matching for mapping fields with similar names.
     * - Uses {@code PRIVATE} access level for accessing fields.
     *
     * This bean facilitates efficient and flexible object mapping while ensuring mappings adhere
     * to the specified configuration.
     *
     * @return a configured {@link ModelMapper} instance customized for detailed and efficient object mapping tasks
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

}