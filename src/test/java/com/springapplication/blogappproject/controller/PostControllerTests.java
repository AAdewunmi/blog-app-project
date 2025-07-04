package com.springapplication.blogappproject.controller;

import com.springapplication.blogappproject.payload.PostDto;
import com.springapplication.blogappproject.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for PostController class.
 * Tests CRUD operations for blog posts.
 */
class PostControllerTests {
    /* Test data for PostDto
     * Used in various test cases to simulate post creation and retrieval.
     */
    private static final String TEST_TITLE = "Test Title";
    private static final String TEST_CONTENT = "Test Content";
    private static final String TEST_DESCRIPTION = "Test Description";
    private static final String NULL_POST_ERROR = "PostDto and its fields must not be null";
    private static final String POST_NOT_FOUND_ERROR = "Post not found";
    private static final Long TEST_POST_ID = 1L;

    private PostService postService;
    private PostController postController;
    private PostDto testPostDto;
    /* * Initializes the PostController and PostService mocks before each test.
     * Creates a test PostDto object to be used in tests.
     */
    @BeforeEach
    void setUp() {
        postService = Mockito.mock(PostService.class);
        postController = new PostController(postService);
        testPostDto = createTestPostDto(null, TEST_TITLE, TEST_CONTENT, TEST_DESCRIPTION);
    }
    /**
     * Tests the createPost method of PostController.
     * Verifies that a post can be created successfully and returns the correct response.
     */
    @Test
    void shouldCreatePostSuccessfully() {
        when(postService.createPost(testPostDto)).thenReturn(testPostDto);

        ResponseEntity<PostDto> response = postController.createPost(testPostDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testPostDto, response.getBody());
        verify(postService).createPost(testPostDto);
    }
    /**
     * Tests the createPost method with a null PostDto.
     * Verifies that an IllegalArgumentException is thrown with the correct message.
     */
    @Test
    void shouldThrowExceptionWhenCreatingNullPost() {
        when(postService.createPost(null)).thenThrow(new IllegalArgumentException(NULL_POST_ERROR));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> postController.createPost(null));

        assertEquals(NULL_POST_ERROR, exception.getMessage());
        verify(postService).createPost(null);
    }
    /**
     * Tests the getAllPosts method of PostController.
     * Verifies that all posts can be retrieved successfully and returns the correct response.
     */
    @Test
    void shouldReturnAllPosts() {
        List<PostDto> expectedPosts = List.of(
                createTestPostDto(1L, "Title1", "Content1", "Description1"),
                createTestPostDto(2L, "Title2", "Content2", "Description2")
        );
        when(postService.getAllPosts()).thenReturn(expectedPosts);

        ResponseEntity<List<PostDto>> response = postController.getAllPosts();
        List<PostDto> actualPosts = response.getBody();

        assertNotNull(actualPosts);
        assertEquals(2, actualPosts.size());
        assertEquals("Title1", actualPosts.get(0).getTitle());
        assertEquals("Title2", actualPosts.get(1).getTitle());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(postService).getAllPosts();
    }
    /**
     * Tests the getPostById method of PostController.
     * Verifies that a post can be retrieved by its ID and returns the correct response.
     */
    @Test
    void shouldReturnPostById() {
        PostDto expectedPost = createTestPostDto(TEST_POST_ID, TEST_TITLE, TEST_CONTENT, TEST_DESCRIPTION);
        when(postService.getPostById(TEST_POST_ID)).thenReturn(expectedPost);

        ResponseEntity<PostDto> response = postController.getPostById(TEST_POST_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPost, response.getBody());
        verify(postService).getPostById(TEST_POST_ID);
    }
    /**
     * Tests the getPostById method with a non-existing post ID.
     * Verifies that an exception is thrown with the correct message.
     */
    @Test
    void shouldThrowExceptionWhenPostNotFound() {
        when(postService.getPostById(TEST_POST_ID)).thenThrow(new RuntimeException(POST_NOT_FOUND_ERROR));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> postController.getPostById(TEST_POST_ID));

        assertEquals(POST_NOT_FOUND_ERROR, exception.getMessage());
        verify(postService).getPostById(TEST_POST_ID);
    }
    /**
     * Tests the deletePostById method of PostController.
     * Verifies that a post can be deleted by its ID and returns the correct response.
     */
    @Test
    void shouldUpdatePostSuccessfully() {
        PostDto updatedPost = createTestPostDto(TEST_POST_ID, "Updated Title", "Updated Content", "Updated Description");
        when(postService.updatePost(testPostDto, TEST_POST_ID)).thenReturn(updatedPost);

        ResponseEntity<PostDto> response = postController.updatePost(testPostDto, TEST_POST_ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedPost, response.getBody());
        verify(postService).updatePost(testPostDto, TEST_POST_ID);
    }
    /**
     * Tests the deletePostById method with a non-existing post ID.
     * Verifies that an exception is thrown with the correct message.
     */
    private PostDto createTestPostDto(Long id, String title, String content, String description) {
        return new PostDto(id, title, content, description);
    }
    /**
     * Tests the updatePost method with a non-existing post ID.
     * Verifies that an exception is thrown with the correct message.
     */
    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentPost() {
        when(postService.updatePost(testPostDto, TEST_POST_ID)).thenThrow(new RuntimeException(POST_NOT_FOUND_ERROR));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> postController.updatePost(testPostDto, TEST_POST_ID));

        assertEquals(POST_NOT_FOUND_ERROR, exception.getMessage());
        verify(postService).updatePost(testPostDto, TEST_POST_ID);
    }


}