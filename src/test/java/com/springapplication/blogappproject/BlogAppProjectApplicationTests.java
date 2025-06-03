package com.springapplication.blogappproject;

import com.springapplication.blogappproject.controller.PostController;
import com.springapplication.blogappproject.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BlogAppProjectApplicationTests {

    @Autowired
    private PostService postService;

    @Autowired
    private PostController postController;

    @Test
    void contextLoads() {
        assertNotNull(postService, "PostService should be initialized");
        assertNotNull(postController, "PostController should be initialized");
    }


}
