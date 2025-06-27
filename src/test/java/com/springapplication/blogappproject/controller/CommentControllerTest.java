package com.springapplication.blogappproject.controller;

import com.springapplication.blogappproject.exception.ResourceNotFoundException;
import com.springapplication.blogappproject.payload.CommentDto;
import com.springapplication.blogappproject.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * Unit tests for the CommentController class.
 * This class verifies the behavior of the CommentController methods using mocked dependencies.
 */
@ExtendWith(MockitoExtension.class)
class CommentControllerTest {
    /**
     * Mocked instance of the CommentService.
     * Used to simulate service layer behavior in controller tests.
     */
    @Mock
    private CommentService commentService;
    /**
     * Injected instance of the CommentController.
     * The controller under test, with its dependencies mocked.
     */
    @InjectMocks
    private CommentController commentController;
    /**
     * A sample CommentDto object used in test cases.
     */
    private CommentDto commentDto;
    /**
     * Sets up the test environment before each test.
     * Initializes the CommentDto object with sample data.
     */
    @BeforeEach
    void setUp() {
        commentDto = new CommentDto();
        commentDto.setBody("Test comment");
        commentDto.setEmail("test@example.com");
        commentDto.setName("Test User");
    }
    /**
     * Tests the createComment method of the CommentController.
     * Verifies that the method returns the created comment with HTTP status 201 (Created).
     */
    @Test
    void createComment_ShouldReturnCreatedComment() {
        // Arrange
        long postId = 1L;
        when(commentService.createComment(eq(postId), any(CommentDto.class)))
                .thenReturn(commentDto);

        // Act
        ResponseEntity<CommentDto> response = commentController.createComment(postId, commentDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(commentDto, response.getBody());
    }
    /**
     * Tests the createComment method with a null CommentDto.
     * Verifies that the method handles null input and returns a created comment with HTTP status 201 (Created).
     */
    @Test
    void createComment_WithNullComment_ShouldReturnCreatedComment() {
        // Arrange
        long postId = 1L;
        CommentDto emptyDto = new CommentDto();
        when(commentService.createComment(eq(postId), any(CommentDto.class)))
                .thenReturn(emptyDto);

        // Act
        ResponseEntity<CommentDto> response = commentController.createComment(postId, emptyDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Verifies that getCommentsByPostId returns an empty list when the post has no comments.
     */
    @Test
    void getCommentsByPostId_ReturnsEmptyList_WhenPostHasNoComments() {
        long postId = 1L;
        when(commentService.getCommentsByPostId(postId)).thenReturn(List.of());

        ResponseEntity<List<CommentDto>> response = commentController.getCommentsByPostId(postId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
    }

    /**
     * Verifies that getCommentsByPostId returns a list of comments when the post has comments.
     */
    @Test
    void getCommentsByPostId_ReturnsCommentsList_WhenPostHasComments() {
        long postId = 1L;
        List<CommentDto> comments = List.of(
                new CommentDto(1L, "User1", "user1@example.com", "Comment 1"),
                new CommentDto(2L, "User2", "user2@example.com", "Comment 2")
        );
        when(commentService.getCommentsByPostId(postId)).thenReturn(comments);

        ResponseEntity<List<CommentDto>> response = commentController.getCommentsByPostId(postId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals(comments, response.getBody());
    }

    /**
     * Verifies that getCommentById returns the correct comment when it exists.
     */
    @Test
    void getCommentById_ReturnsComment_WhenCommentExists() {
        long postId = 1L;
        long commentId = 1L;
        CommentDto comment = new CommentDto(commentId, "User1", "user1@example.com", "Comment 1");
        when(commentService.getCommentById(postId, commentId)).thenReturn(comment);

        ResponseEntity<CommentDto> response = commentController.getCommentById(postId, commentId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(comment, response.getBody());
    }

    /**
     * Verifies that getCommentById throws an exception when the comment does not exist.
     */
    @Test
    void getCommentById_ThrowsException_WhenCommentDoesNotExist() {
        long postId = 1L;
        long commentId = 999L;
        when(commentService.getCommentById(postId, commentId))
                .thenThrow(new ResourceNotFoundException("Comment", "id", commentId));

        assertThrows(ResourceNotFoundException.class, () -> commentController.getCommentById(postId, commentId));
    }
    /**
     * Tests the updateComment method of the CommentController.
     * Verifies that the method successfully updates a comment and returns the updated comment with HTTP status 200 (OK).
     */
    @Test
    void testUpdateComment_ShouldUpdateComment() {
        // Arrange
        long postId = 1L;
        long commentId = 1L;
        CommentDto updatedCommentDto = new CommentDto(commentId, "Updated User", "updated@example.com", "Updated Comment");
        when(commentService.updateComment(eq(postId), eq(commentId), any(CommentDto.class)))
                .thenReturn(updatedCommentDto);

        // Act
        ResponseEntity<CommentDto> response = commentController.updateComment(postId, commentId, updatedCommentDto);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedCommentDto, response.getBody());
    }

    /**
     * Tests the updateComment method with a non-existing comment ID.
     * Verifies that the method throws a ResourceNotFoundException when the comment does not exist.
     */
    @Test
    void testUpdateComment_ThrowsException_WhenCommentDoesNotExist() {
        // Arrange
        long postId = 1L;
        long commentId = 999L;
        CommentDto updatedCommentDto = new CommentDto(commentId, "Updated User", "updated@example.com", "Updated Comment");
        when(commentService.updateComment(eq(postId), eq(commentId), any(CommentDto.class)))
                .thenThrow(new ResourceNotFoundException("Comment", "id", commentId));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> commentController.updateComment(postId, commentId, updatedCommentDto));
    }


    /**
     * Tests the deleteComment method of the CommentController.
     * Verifies that the method successfully deletes a comment and returns HTTP status 204 (No Content).
     */
    @Test
    void testDeleteComment_ShouldDeleteComment() {
        // Arrange
        long postId = 1L;
        long commentId = 1L;

        doNothing().when(commentService).deleteComment(postId, commentId);

        // Act
        ResponseEntity<String> response = commentController.deleteComment(postId, commentId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Tests the deleteComment method with a non-existing comment ID.
     * Verifies that the method throws a ResourceNotFoundException when the comment does not exist.
     */
    @Test
    void testDeleteComment_ShouldThrowException_WhenCommentDoesNotExist() {
        // Arrange
        long postId = 1L;
        long commentId = 999L;

        doThrow(new ResourceNotFoundException("Comment", "id", commentId))
                .when(commentService).deleteComment(postId, commentId);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> commentController.deleteComment(postId, commentId));
    }
}