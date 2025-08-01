package com.springapplication.blogappproject.config;

import com.springapplication.blogappproject.security.JwtAuthenticationFilter;
import com.springapplication.blogappproject.service.CustomUserDetailsService;
import com.springapplication.blogappproject.security.JwtAuthenticationEntryPoint;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Configuration class for security settings in a Spring Boot application.
 *
 * This class leverages Spring Security to configure HTTP security settings, authentication,
 * and password encoding mechanisms. It also provides a configured ModelMapper for object mapping tasks.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                        JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                        JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Creates and returns a bean of type {@link PasswordEncoder}.
     * The implementation used is {@link BCryptPasswordEncoder}, which provides a secure way to encode passwords
     * by employing the bcrypt hashing algorithm.
     *
     * @return a {@link PasswordEncoder} instance that uses bcrypt for password encoding
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures and builds the security filter chain for the application.
     * This method is responsible for defining the security policies, including
     * disabling CSRF protection, configuring authorization rules, setting the
     * HTTP Basic authentication mechanism, defining the custom authentication provider,
     * handling authentication exceptions through a custom entry point, and enforcing stateless
     * session management for API security.
     *
     * @param http the {@link HttpSecurity} object used to configure security settings
     *             for specific HTTP requests and resources
     * @return the built {@link SecurityFilterChain} configured with the application's security policies
     * @throws Exception if an error occurs while setting up the security configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/**").permitAll()
//                .requestMatchers("/api/auth/**").permitAll()
//                .anyRequest().authenticated()
//            )
//            .httpBasic(Customizer.withDefaults())
//            .authenticationProvider(authenticationProvider())  // Add this line
//            .exceptionHandling(exception ->
//                //exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
//                    exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
//            )
//            .sessionManagement(session -> session
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            );
//        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        //.requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/**").authenticated()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
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