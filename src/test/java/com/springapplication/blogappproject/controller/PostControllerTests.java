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
import java.util.List;

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

    @Test
    @DisplayName("CreatePost throws IllegalArgumentException when PostDto is null")
    void createPostThrowsExceptionWhenPostDtoIsNull() {
        PostDto postDto = null;

        when(postService.createPost(postDto)).thenThrow(new IllegalArgumentException("PostDto and its fields must not be null"));

        try {
            postController.createPost(postDto);
        } catch (IllegalArgumentException e) {
            assertEquals("PostDto and its fields must not be null", e.getMessage());
        }

        verify(postService, times(1)).createPost(postDto);
    }

    @Test
    @DisplayName("GetAllPosts returns a list of PostDto when posts exist")
    void getAllPostsReturnsListOfPostDto() {
        List<PostDto> postDtos = List.of(
                new PostDto(1L, "Title1", "Content1", "Description1"),
                new PostDto(2L, "Title2", "Content2", "Description2")
        );

        when(postService.getAllPosts()).thenReturn(postDtos);

        List<PostDto> response = postController.getAllPosts();

        assertEquals(2, response.size());
        assertEquals("Title1", response.get(0).getTitle());
        assertEquals("Title2", response.get(1).getTitle());
        verify(postService, times(1)).getAllPosts();
    }

    @Test
    @DisplayName("GetPostById returns PostDto when post with given ID exists")
    void getPostByIdReturnsPostDtoWhenPostExists() {
        PostDto postDto = new PostDto(1L, "Title", "Content", "Description");

        when(postService.getPostById(1L)).thenReturn(postDto);

        ResponseEntity<PostDto> response = postController.getPostById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postDto, response.getBody());
        verify(postService, times(1)).getPostById(1L);
    }

    @Test
    @DisplayName("GetPostById throws exception when post with given ID does not exist")
    void getPostByIdThrowsExceptionWhenPostDoesNotExist() {
        when(postService.getPostById(1L)).thenThrow(new RuntimeException("Post not found"));

        try {
            postController.getPostById(1L);
        } catch (RuntimeException e) {
            assertEquals("Post not found", e.getMessage());
        }

        verify(postService, times(1)).getPostById(1L);
    }

    @Test
    @DisplayName("UpdatePost returns updated PostDto when valid input is provided")
    void updatePostReturnsUpdatedPostDto() {
        PostDto postDto = new PostDto(null, "Updated Title", "Updated Content", "Updated Description");
        PostDto updatedPostDto = new PostDto(1L, "Updated Title", "Updated Content", "Updated Description");

        when(postService.updatePost(postDto, 1L)).thenReturn(updatedPostDto);

        ResponseEntity<PostDto> response = postController.updatePost(postDto, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedPostDto, response.getBody());
        verify(postService, times(1)).updatePost(postDto, 1L);
    }

    @Test
    @DisplayName("UpdatePost throws exception when post with given ID does not exist")
    void updatePostThrowsExceptionWhenPostDoesNotExist() {
        PostDto postDto = new PostDto(null, "Updated Title", "Updated Content", "Updated Description");

        when(postService.updatePost(postDto, 1L)).thenThrow(new RuntimeException("Post not found"));

        try {
            postController.updatePost(postDto, 1L);
        } catch (RuntimeException e) {
            assertEquals("Post not found", e.getMessage());
        }

        verify(postService, times(1)).updatePost(postDto, 1L);
    }
}
