package com.springapplication.blogappproject.service.impl;

import com.springapplication.blogappproject.entity.Comment;
import com.springapplication.blogappproject.entity.Post;
import com.springapplication.blogappproject.exception.BlogAPIException;
import com.springapplication.blogappproject.exception.ResourceNotFoundException;
import com.springapplication.blogappproject.payload.CommentDto;
import com.springapplication.blogappproject.repository.CommentRepository;
import com.springapplication.blogappproject.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

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
    /**
     * Tests the retrieval of comments by post ID.
     * Verifies that the correct comments are returned for a given post ID.
     */
    @Test
    void getCommentsByPostIdReturnsEmptyListWhenNoCommentsExist() {
        long postId = 1L;

        when(commentRepository.findByPostId(postId)).thenReturn(List.of());

        List<CommentDto> comments = commentService.getCommentsByPostId(postId);

        assertEquals(0, comments.size());
        verify(commentRepository, times(1)).findByPostId(postId);
    }
    /**
     * Tests the scenario where a comment does not belong to the specified post.
     * Verifies that a BlogAPIException is thrown when attempting to retrieve the comment.
     */
    @Test
    void getCommentByIdThrowsExceptionWhenCommentDoesNotBelongToPost() {
        long postId = 1L;
        long commentId = 2L;

        Post post = new Post();
        post.setId(postId);

        Comment comment = new Comment();
        comment.setId(commentId);

        // Set the comment's post to a different post with a non-null id
        Post differentPost = new Post();
        differentPost.setId(999L); // Different from postId
        comment.setPost(differentPost);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        assertThrows(BlogAPIException.class, () -> commentService.getCommentById(postId, commentId));

        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).findById(commentId);
    }

    /**
     * Tests the successful update of a comment.
     * Verifies that the updated comment data is correctly returned.
     */
    @Test
    void testUpdateCommentSuccess() {
        // Arrange
        long postId = 1L;
        long commentId = 2L;

        CommentDto existingCommentDto = new CommentDto();
        existingCommentDto.setId(commentId);
        existingCommentDto.setName("Old Name");
        existingCommentDto.setEmail("oldemail@example.com");
        existingCommentDto.setBody("Old body");

        CommentDto updatedCommentDto = new CommentDto();
        updatedCommentDto.setName("New Name");
        updatedCommentDto.setEmail("newemail@example.com");
        updatedCommentDto.setBody("Updated body");

        Post post = new Post();
        post.setId(postId);

        Comment existingComment = new Comment();
        existingComment.setId(commentId);
        existingComment.setName("Old Name");
        existingComment.setEmail("oldemail@example.com");
        existingComment.setBody("Old body");
        existingComment.setPost(post);

        Comment updatedComment = new Comment();
        updatedComment.setId(commentId);
        updatedComment.setName("New Name");
        updatedComment.setEmail("newemail@example.com");
        updatedComment.setBody("Updated body");
        updatedComment.setPost(post);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(updatedComment);

        // Act
        CommentDto result = commentService.updateComment(postId, commentId, updatedCommentDto);

        // Assert
        assertEquals(updatedCommentDto.getName(), result.getName());
        assertEquals(updatedCommentDto.getEmail(), result.getEmail());
        assertEquals(updatedCommentDto.getBody(), result.getBody());

        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    /**
     * Tests the scenario where the post associated with the comment to update does not exist.
     * Verifies that a ResourceNotFoundException is thrown.
     */
    @Test
    void testUpdateCommentPostNotFound() {
        // Arrange
        long postId = 99L;
        long commentId = 1L;
        CommentDto updatedCommentDto = new CommentDto();
        updatedCommentDto.setName("New Name");
        updatedCommentDto.setEmail("newemail@example.com");
        updatedCommentDto.setBody("Updated body");

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                commentService.updateComment(postId, commentId, updatedCommentDto));

        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, never()).findById(commentId);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    /**
     * Tests the scenario where the comment to update does not exist.
     * Verifies that a ResourceNotFoundException is thrown.
     */
    @Test
    void testUpdateCommentCommentNotFound() {
        // Arrange
        long postId = 1L;
        long commentId = 99L;
        CommentDto updatedCommentDto = new CommentDto();
        updatedCommentDto.setName("New Name");
        updatedCommentDto.setEmail("newemail@example.com");
        updatedCommentDto.setBody("Updated body");

        Post post = new Post();
        post.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                commentService.updateComment(postId, commentId, updatedCommentDto));

        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, never()).save(any(Comment.class));
    }
}