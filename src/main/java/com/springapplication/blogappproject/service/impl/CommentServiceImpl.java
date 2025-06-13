package com.springapplication.blogappproject.service.impl;

import com.springapplication.blogappproject.entity.Comment;
import com.springapplication.blogappproject.payload.CommentDto;
import com.springapplication.blogappproject.repository.CommentRepository;
import com.springapplication.blogappproject.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CommentServiceImpl is the implementation of the CommentService interface.
 * It provides methods to handle comment-related operations.
 */
@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    /**
     * Constructor for CommentServiceImpl.
     * @param commentRepository The repository for managing comments.
     */
    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    /**
     * Creates a new comment for a specific post.
     * @param postId The ID of the post to which the comment belongs.
     * @param commentDto The data transfer object containing comment details.
     * @return The created CommentDto object.
     */
    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        // Implementation for creating a comment
        return null; // Placeholder return statement
    }
    /**
     * Retrieves a comment by its ID.
     * @param comment The ID of the comment to retrieve.
     * @return The CommentDto object representing the retrieved comment.
     */
    private CommentDto mapToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());
        return commentDto;
    }
}
