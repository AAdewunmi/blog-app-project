package com.springapplication.blogappproject.controller;

import com.springapplication.blogappproject.payload.PostDto;
import com.springapplication.blogappproject.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PostControllerTests {

    private PostService postService;
    private PostController postController;

    @BeforeEach
    void setUp() {
        postService = Mockito.mock(PostService.class);
        postController = new PostController(postService);
    }

    @Test
    @DisplayName("CreatePost returns CREATED status and PostDto when valid input is provided")
    void createPostReturnsCreatedStatusAndPostDto() {
        PostDto postDto = new PostDto();
        postDto.setTitle("Title");
        postDto.setContent("Content");
        postDto.setDescription("Description");

        when(postService.createPost(postDto)).thenReturn(postDto);

        ResponseEntity<PostDto> response = postController.createPost(postDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(postDto, response.getBody());
        verify(postService, times(1)).createPost(postDto);
    }

    
}
