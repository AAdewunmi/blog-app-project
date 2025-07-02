package com.springapplication.blogappproject;

import com.springapplication.blogappproject.controller.PostController;
import com.springapplication.blogappproject.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    /**
     * Verifies that the ModelMapper bean is correctly initialized.
     */
    @Test
    @DisplayName("ModelMapper Bean Initialization")
    void modelMapperBean_IsInitialized() {
        ModelMapper modelMapper = applicationContext.getBean(ModelMapper.class);
        assertNotNull(modelMapper, "ModelMapper bean should be properly initialized");
    }
    /**
     * Tests the direct invocation of the modelMapper method in BlogAppProjectApplication.
     */
    @Test
    @DisplayName("Direct ModelMapper Bean Instance Test")
    void testModelMapperBean_ReturnsModelMapperInstance() {
        BlogAppProjectApplication application = new BlogAppProjectApplication();
        /**
         * Configuration class for mapping-related beans and settings.
         * Separates mapping concerns from security configuration.
         */
        final ModelMapper[] modelMapper = new ModelMapper[1];
        @Configuration
        class MappingConfig {

            /**
             * Creates and configures a ModelMapper bean with custom settings.
             * This bean is used for object mapping throughout the application.
             *
             * @return configured ModelMapper instance
             */
            @Bean
            public ModelMapper modelMapper() {
                modelMapper[0] = new ModelMapper();
                modelMapper[0].getConfiguration()
                        .setPropertyCondition(context -> context.getSource() != null)
                        .setSkipNullEnabled(true)
                        .setAmbiguityIgnored(true)
                        .setCollectionsMergeEnabled(false)
                        .setFieldMatchingEnabled(true)
                        .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
                return modelMapper[0];
            }
        }
        assertNotNull(modelMapper[0], "The modelMapper() method should return a non-null instance of ModelMapper");
    }
}