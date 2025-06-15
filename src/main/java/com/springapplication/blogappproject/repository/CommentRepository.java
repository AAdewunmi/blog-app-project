package com.springapplication.blogappproject.repository;

import com.springapplication.blogappproject.entity.Comment;
import com.springapplication.blogappproject.payload.CommentDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing Comment entities.
 * This interface extends JpaRepository to provide CRUD operations for Comment entities.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Creates a new comment associated with a specific post ID.
     *
     * @param postId the ID of the post to which the comment is associated
     * @param commentDto the data transfer object containing comment details
     * @return the created CommentDto
     */
    CommentDto createComment(long postId, CommentDto commentDto);

    /**
     * Retrieves all comments associated with a specific post ID.
     *
     * @param postId the ID of the post for which comments are to be retrieved
     * @return a list of CommentDto objects associated with the specified post ID
     */
   List<CommentDto> getCommentsByPostId(long postId);


}
