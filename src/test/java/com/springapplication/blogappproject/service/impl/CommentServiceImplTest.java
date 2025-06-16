package com.springapplication.blogappproject.service.impl;

import com.springapplication.blogappproject.entity.Comment;
import com.springapplication.blogappproject.entity.Post;
import com.springapplication.blogappproject.exception.ResourceNotFoundException;
import com.springapplication.blogappproject.payload.CommentDto;
import com.springapplication.blogappproject.repository.CommentRepository;
import com.springapplication.blogappproject.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
/**
 * Unit tests for the CommentServiceImpl class.
 * This class tests the createComment method of the CommentServiceImpl.
 */
@SpringBootTest
public class CommentServiceImplTest {
    /**
     * The service being tested.
     */
    @Autowired
    private CommentServiceImpl commentService;
    /**
     * Mocked repository for managing Comment entities.
     */
    @MockitoBean
    private CommentRepository commentRepository;
    /**
     * Mocked repository for managing Post entities.
     */
    @MockitoBean
    private PostRepository postRepository;
    /**
     * Tests the successful creation of a comment.
     * Verifies that the comment is saved and associated with the correct post.
     */
    @Test
    void testCreateCommentSuccess() {
        // Arrange
        long postId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setName("John Doe");
        commentDto.setEmail("johndoe@example.com");
        commentDto.setBody("This is a test comment.");

        Post post = new Post();
        post.setId(postId);
        post.setTitle("Test Post");
        post.setContent("Test content");

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setName("John Doe");
        comment.setEmail("johndoe@example.com");
        comment.setBody("This is a test comment.");
        comment.setPost(post);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Act
        CommentDto result = commentService.createComment(postId, commentDto);

        // Assert
        assertEquals(commentDto.getName(), result.getName());
        assertEquals(commentDto.getEmail(), result.getEmail());
        assertEquals(commentDto.getBody(), result.getBody());

        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }
    /**
     * Tests the scenario where the post associated with the comment does not exist.
     * Verifies that a ResourceNotFoundException is thrown.
     */
    @Test
    void testCreateCommentPostNotFound() {
        // Arrange
        long postId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setId(2L);
        commentDto.setName("Jane Doe");
        commentDto.setEmail("janedoe@example.com");
        commentDto.setBody("Another test comment.");

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> commentService.createComment(postId, commentDto));

        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, never()).save(any(Comment.class));
    }
    /**
     * Tests the scenario where an invalid post ID is provided when creating a comment.
     * Verifies that a ResourceNotFoundException is thrown.
     */
    @Test
    void createCommentThrowsExceptionWhenPostIdIsInvalid() {
        long invalidPostId = 999L;
        CommentDto commentDto = new CommentDto();
        commentDto.setName("Invalid Post Test");
        commentDto.setEmail("invalid@example.com");
        commentDto.setBody("This comment should fail.");

        when(postRepository.findById(invalidPostId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commentService.createComment(invalidPostId, commentDto));

        verify(postRepository, times(1)).findById(invalidPostId);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void getCommentsByPostIdReturnsEmptyListWhenNoCommentsExist() {
        long postId = 1L;

        when(commentRepository.findByPostId(postId)).thenReturn(List.of());

        List<CommentDto> comments = commentService.getCommentsByPostId(postId);

        assertEquals(0, comments.size());
        verify(commentRepository, times(1)).findByPostId(postId);
    }
}