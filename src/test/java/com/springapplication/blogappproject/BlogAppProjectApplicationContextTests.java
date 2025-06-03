package com.springapplication.blogappproject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BlogAppProjectApplicationTests_1 {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext, "Application context should not be null");
    }

    @Test
    void applicationStartsWithoutErrors() {
        assertTrue(applicationContext.getBeanDefinitionCount() > 0, 
            "Application should have registered beans");
    }

    @Test
    void requiredBeansArePresent() {
        // Check for essential beans that should be present in your application
        assertTrue(applicationContext.containsBean("postServiceImpl"), 
            "PostServiceImpl bean should be present");
        assertTrue(applicationContext.containsBean("postRepository"), 
            "PostRepository bean should be present");
    }

    @Test
    void activeProfilesAreConfigured() {
        // Verify that expected profiles are active
        assertArrayEquals(
            new String[]{"default"}, 
            applicationContext.getEnvironment().getActiveProfiles(),
            "Default profile should be active in test environment"
        );
    }

    @Test
    void databaseConfigurationIsPresent() {
        assertTrue(
            applicationContext.getEnvironment().containsProperty("spring.datasource.url"),
            "Database URL configuration should be present"
        );
        assertTrue(
            applicationContext.getEnvironment().containsProperty("spring.jpa.hibernate.ddl-auto"),
            "Hibernate DDL configuration should be present"
        );
    }
}