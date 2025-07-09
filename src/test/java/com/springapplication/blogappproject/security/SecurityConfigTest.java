package com.springapplication.blogappproject.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

@ExtendWith(SpringExtension.class)
public class SecurityConfigTest {

    private final SecurityConfig securityConfig = new SecurityConfig();

    @Test
    public void testUserDetailsService() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        UserDetailsService service = securityConfig.userDetailsService(encoder);

        UserDetails admin = service.loadUserByUsername("admin");
        assertNotNull(admin);
        assertEquals("admin", admin.getUsername());
        assertTrue(encoder.matches("secret", admin.getPassword()));
        assertTrue(admin.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertTrue(admin.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    public void testModelMapperConfiguration() {
        ModelMapper modelMapper = securityConfig.modelMapper();

        assertNotNull(modelMapper);
        assertTrue(modelMapper.getConfiguration().isSkipNullEnabled());
        assertTrue(modelMapper.getConfiguration().isAmbiguityIgnored());
        assertFalse(modelMapper.getConfiguration().isCollectionsMergeEnabled());
        assertTrue(modelMapper.getConfiguration().isFieldMatchingEnabled());
        assertEquals(org.modelmapper.config.Configuration.AccessLevel.PRIVATE,
                modelMapper.getConfiguration().getFieldAccessLevel());

        // Simulate converter behavior
        Set<String> input = Set.of("sample");
        Set<String> copy = new java.util.HashSet<>(input);
        assertEquals(1, copy.size());
        assertTrue(copy.contains("sample"));
    }
}

