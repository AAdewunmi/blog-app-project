package com.springapplication.blogappproject;

import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main application class for the Blog App Project.
 * This class serves as the entry point for the Spring Boot application.
 * It uses the @SpringBootApplication annotation to enable auto-configuration,
 * component scanning, and configuration properties.
 */
@SpringBootApplication
public class BlogAppProjectApplication {
    /**
     * Bean definition for ModelMapper.
     * This method creates and returns a new instance of ModelMapper,
     * which is used for object mapping between different layers of the application.
     *
     * @return a new instance of ModelMapper
     */

    /**
     * The main method that starts the Spring Boot application.
     *
     * @param args command line arguments (not used in this application)
     */
    public static void main(String[] args) {
        SpringApplication.run(BlogAppProjectApplication.class, args);
    }

}