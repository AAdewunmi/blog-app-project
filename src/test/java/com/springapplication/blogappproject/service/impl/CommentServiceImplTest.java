package com.springapplication.blogappproject.service.impl;

import com.springapplication.blogappproject.entity.Comment;
import com.springapplication.blogappproject.entity.Post;
import com.springapplication.blogappproject.exception.BlogAPIException;
import com.springapplication.blogappproject.exception.ResourceNotFoundException;
import com.springapplication.blogappproject.payload.CommentDto;
import com.springapplication.blogappproject.repository.CommentRepository;
import com.springapplication.blogappproject.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
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
    @MockitoBean
    private ModelMapper modelMapper;

    @Autowired
    public CommentServiceImplTest(CommentRepository commentRepository,
                                  PostRepository postRepository,
                                  ModelMapper modelMapper) {
        this.commentService = new CommentServiceImpl(commentRepository, postRepository, modelMapper);
    }

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
     * Tests the creation of a comment with invalid data.
     *
     * This test verifies that an exception is thrown when attempting to create a comment
     * with invalid or missing required fields. Specifically, it checks:
     * - The name field is null.
     * - The body field is an empty string.
     *
     * Expected behavior:
     * - An IllegalArgumentException is thrown with the message "Invalid comment data provided".
     * - The post is retrieved from the repository.
     * - The comment is not saved in the repository.
     *
     * Test scenario:
     * 1. A CommentDto object is created with invalid name and body fields.
     * 2. The associated post is mocked to exist in the repository.
     * 3. The createComment method is called with the invalid data and the expected exception is asserted.
     * 4. Verification is performed to ensure the comment repository is not invoked for saving.
     */
    @Test
    void testCreateCommentWithInvalidData() {
        // Arrange
        long postId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setName(null); // Invalid name field
        commentDto.setEmail("johndoe@example.com");
        commentDto.setBody(""); // Invalid body field

        Post post = new Post();
        post.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> commentService.createComment(postId, commentDto));
        assertEquals("Invalid comment data provided", exception.getMessage());

        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    /**
     * Tests the creation of a comment with an invalid post ID.
     *
     * This test ensures that when an invalid post ID is provided for creating a comment,
     * the `createComment` method throws an `IllegalArgumentException`. The conditions tested include:
     * - The post ID provided is invalid (e.g., 0 or negative).
     * - The comment is not saved in the repository.
     * - The post repository is not queried for the invalid ID.
     *
     * Test scenario:
     * 1. A `CommentDto` object is prepared with valid fields for name, email, and body.
     * 2. An invalid post ID is provided.
     * 3. The `createComment` method is called with the invalid post ID and `CommentDto`.
     * 4. An `IllegalArgumentException` is expected to be thrown.
     * 5. Verification is performed that neither the post repository nor the comment repository is invoked.
     */
    @Test
    void testCreateCommentWithInvalidPostId() {
        // Arrange
        long postId = 0L; // Invalid post ID
        CommentDto commentDto = new CommentDto();
        commentDto.setName("John Doe");
        commentDto.setEmail("johndoe@example.com");
        commentDto.setBody("This is a test comment.");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> commentService.createComment(postId, commentDto));

        verify(postRepository, never()).findById(postId);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    /**
     * Tests the scenario where creating a comment fails due to an issue during the save operation.
     *
     * This test ensures the following:
     * 1. The post to associate the comment with exists in the repository.
     * 2. When the save operation on the comment repository throws a runtime exception (e.g., database error),
     *    the exception is propagated correctly.
     *
     * Verification:
     * - The post repository is queried once for the post ID.
     * - The comment repository's save method is called once.
     * - The correct exception is thrown with the expected message.
     */
    @Test
    void testCreateCommentSaveFailure() {
        // Arrange
        long postId = 1L;
        CommentDto commentDto = new CommentDto();
        commentDto.setName("John Doe");
        commentDto.setEmail("johndoe@example.com");
        commentDto.setBody("This is a test comment.");

        Post post = new Post();
        post.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> commentService.createComment(postId, commentDto));
        assertEquals("Database error", exception.getMessage());

        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).save(any(Comment.class));
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

    /**
     * Tests the scenario where an attempt is made to update a comment that does not belong to the specified post.
     * Verifies that a BlogAPIException is thrown.
     */
    @Test
    void testUpdateCommentThrowsExceptionWhenCommentDoesNotBelongToPost() {
        // Arrange
        long postId = 1L;
        long commentId = 2L;

        CommentDto updatedCommentDto = new CommentDto();
        updatedCommentDto.setName("New Name");
        updatedCommentDto.setEmail("newemail@example.com");
        updatedCommentDto.setBody("Updated body");

        Post post = new Post();
        post.setId(postId);

        Post differentPost = new Post();
        differentPost.setId(99L);

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setPost(differentPost);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act & Assert
        assertThrows(BlogAPIException.class, () ->
                commentService.updateComment(postId, commentId, updatedCommentDto));

        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    /**
     * Tests the successful deletion of a comment.
     */
    @Test
    void testDeleteCommentSuccess() {
        // Arrange
        long postId = 1L;
        long commentId = 2L;

        Post post = new Post();
        post.setId(postId);

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setPost(post);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act
        commentService.deleteComment(postId, commentId);

        // Assert
        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).delete(comment);
    }

    /**
     * Tests the scenario where the post associated with the comment does not exist.
     * Verifies that a ResourceNotFoundException is thrown.
     */
    @Test
    void testDeleteCommentPostNotFound() {
        // Arrange
        long postId = 99L;
        long commentId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> commentService.deleteComment(postId, commentId));

        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, never()).findById(commentId);
        verify(commentRepository, never()).delete(any(Comment.class));
    }

    /**
     * Tests the scenario where the comment to delete does not exist.
     * Verifies that a ResourceNotFoundException is thrown.
     */
    @Test
    void testDeleteCommentCommentNotFound() {
        // Arrange
        long postId = 1L;
        long commentId = 99L;

        Post post = new Post();
        post.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> commentService.deleteComment(postId, commentId));

        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, never()).delete(any(Comment.class));
    }

    /**
     * Tests the scenario where an attempt is made to delete a comment that does not belong to the specified post.
     * Verifies that a BlogAPIException is thrown.
     */
    @Test
    void testDeleteCommentThrowsExceptionWhenCommentDoesNotBelongToPost() {
        // Arrange
        long postId = 1L;
        long commentId = 2L;

        Post post = new Post();
        post.setId(postId);

        Post differentPost = new Post();
        differentPost.setId(99L);

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setPost(differentPost);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act & Assert
        assertThrows(BlogAPIException.class, () -> commentService.deleteComment(postId, commentId));

        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, never()).delete(any(Comment.class));
    }
}