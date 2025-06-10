package com.springapplication.blogappproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * Main application class for the Blog App Project.
 * This class serves as the entry point for the Spring Boot application.
 * It uses the @SpringBootApplication annotation to enable auto-configuration,
 * component scanning, and configuration properties.
 */
@SpringBootApplication
public class BlogAppProjectApplication {
    /**
     * The main method that starts the Spring Boot application.
     *
     * @param args command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        SpringApplication.run(BlogAppProjectApplication.class, args);
    }

}
