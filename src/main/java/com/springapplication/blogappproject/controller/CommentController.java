package com.springapplication.blogappproject.controller;

import com.springapplication.blogappproject.payload.CommentDto;
import com.springapplication.blogappproject.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for managing blog post comments in the application.
 * Provides REST API endpoints for creating, retrieving, updating, and deleting comments
 * associated with specific blog posts.
 */


@RestController
@RequestMapping("/api")
public class CommentController {

    /**
     * Service instance responsible for managing comment-related operations in the application.
     * Provides functionality to create, retrieve, update, and delete comments associated with blog posts.
     */
    private final CommentService commentService;

    /**
     * Constructs a new instance of {@code CommentController}.
     *
     * @param commentService the service responsible for managing comment operations
     */
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Creates a new comment for a specific blog post.
     *
     * @param postId the ID of the post to which the comment is associated
     * @param commentDto the data transfer object containing the details of the comment to be created
     * @return a ResponseEntity containing the created CommentDto and HTTP status code
     */
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(
            @PathVariable("postId") long postId,
            @Valid @RequestBody CommentDto commentDto) {

        CommentDto created = commentService.createComment(postId, commentDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Retrieves all comments associated with a specific blog post.
     *
     * @param postId the ID of the post for which comments are to be retrieved
     * @return a ResponseEntity containing a list of CommentDto objects and HTTP status code
     */
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable("postId") long postId) {
        List<CommentDto> comments = commentService.getCommentsByPostId(postId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
    /**
     * Retrieves a specific comment by its ID associated with a specific blog post.
     *
     * @param postId the ID of the post to which the comment is associated
     * @param commentId the ID of the comment to be retrieved
     * @return a ResponseEntity containing the CommentDto and HTTP status code
     */
    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable("postId") long postId, @PathVariable("id") long commentId) {
        CommentDto comment = commentService.getCommentById(postId, commentId);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    /**
     * Updates an existing comment associated with a specific blog post.

     * @param postId the ID of the post with which the comment is associated
     * @param commentId the ID of the comment to be updated
     * @param commentDto the data transfer object containing the updated details of the comment
     * @return a ResponseEntity containing the updated CommentDto and HTTP status code
     */
    @PutMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable("postId") long postId,
            @PathVariable("id") long commentId,
            @Valid @RequestBody CommentDto commentDto) {
        CommentDto updatedComment = commentService.updateComment(postId, commentId, commentDto);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    /**
     * Deletes a specific comment associated with a specific blog post.
     *
     * @param postId the ID of the post with which the comment is associated
     * @param commentId the ID of the comment to be deleted
     * @return a ResponseEntity with HTTP status code indicating the result of the deletion
     */

    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("postId") long postId, @PathVariable("id") long commentId) {
        commentService.deleteComment(postId, commentId);
        return new ResponseEntity<>("Comment deleted successfully!", HttpStatus.NO_CONTENT);
    }
}
