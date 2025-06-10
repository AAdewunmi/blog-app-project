package com.springapplication.blogappproject;

import com.springapplication.blogappproject.controller.PostController;
import com.springapplication.blogappproject.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Integration tests for BlogAppProject application.
 * Verifies that the Spring application context loads correctly
 * and all required beans are properly configured.
 */
@SpringBootTest
class BlogAppProjectApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PostService postService;

    @Autowired
    private PostController postController;
    /*
     * Note: The PostService and PostController are autowired to ensure that
     * they are correctly initialized by the Spring context.
     * If these beans are not found, the test will fail, indicating a configuration issue.
     */
    @Test
    @DisplayName("Spring Context Load Test")
    void contextLoads() {
        assertAll("Spring Context Loading",
                () -> assertNotNull(applicationContext, "Application context should not be null"),
                () -> assertNotNull(postService, "PostService should be properly initialized"),
                () -> assertNotNull(postController, "PostController should be properly initialized")
        );
    }
}