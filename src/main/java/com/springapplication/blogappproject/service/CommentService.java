package com.springapplication.blogappproject.service;

import com.springapplication.blogappproject.payload.CommentDto;

import java.util.List;

/**
 * Service interface for managing comments in the blog application.
 * This interface defines methods for creating and managing comments associated with blog posts.
 */
public interface CommentService {
    /**
     * Creates a new comment for a specific blog post.
     *
     * @param postId the ID of the post to which the comment is associated
     * @param commentDto the data transfer object containing the details of the comment to be created
     * @return the created CommentDto object
     */
    CommentDto createComment(long postId, CommentDto commentDto);

    /**
     * Retrieves a list of comments associated with a specific blog post by ID.
     *
     * @param postId the ID of the post for which comments are to be retrieved
     * @return a list of CommentDto objects associated with the specified post-ID
     */

    List<CommentDto> getCommentsByPostId(long postId);

    /**
     * Retrieves all comments associated with a specific blog post and a specific comment ID.
     *
     * @param postId the ID of the post from which the comment is to be deleted
     * @param commentId the ID of the comment to be deleted
     */
    CommentDto getCommentById(long postId, long commentId);

    /**
     * Updates a comment associated with a specific blog post.
     * @param postId the ID of the post from which the comment is to be deleted
     * @param commentId the ID of the comment to be deleted
     * @param commentDto the data transfer object containing the updated details of the comment
     *
     */

    CommentDto updateComment(long postId, long commentId, CommentDto commentDto);

    /**
     * Deletes a comment associated with a specific blog post.
     *
     * @param postId the ID of the post from which the comment is to be deleted
     * @param commentId the ID of the comment to be deleted
     */
    void deleteComment(long postId, long commentId);
}
