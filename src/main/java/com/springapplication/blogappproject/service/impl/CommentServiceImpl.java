package com.springapplication.blogappproject.service.impl;

import com.springapplication.blogappproject.entity.Comment;
import com.springapplication.blogappproject.entity.Post;
import com.springapplication.blogappproject.exception.ResourceNotFoundException;
import com.springapplication.blogappproject.payload.CommentDto;
import com.springapplication.blogappproject.repository.CommentRepository;
import com.springapplication.blogappproject.repository.PostRepository;
import com.springapplication.blogappproject.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CommentServiceImpl is the implementation of the CommentService interface.
 * It provides methods to handle comment-related operations.
 */
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    /**
     * Constructor for CommentServiceImpl.
     * @param commentRepository The repository for managing comments.
     */
    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }
    /**
     * Creates a new comment for a specific post.
     * @param postId The ID of the post to which the comment belongs.
     * @param commentDto The data transfer object containing comment details.
     * @return The created CommentDto object.
     */
    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mapToEntity(commentDto);

        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // set post to comment entity
        comment.setPost(post);

        // comment entity to DB
        Comment newComment =  commentRepository.save(comment);

        return mapToDto(newComment);
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
    /**
     * Maps a CommentDto object to a Comment entity.
     * @param commentDto The CommentDto object to map.
     * @return The Comment entity.
     */
    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        return comment;
    }
}
