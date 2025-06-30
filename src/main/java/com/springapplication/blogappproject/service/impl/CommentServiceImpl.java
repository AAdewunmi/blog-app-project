package com.springapplication.blogappproject.service.impl;

import com.springapplication.blogappproject.entity.Comment;
import com.springapplication.blogappproject.entity.Post;
import com.springapplication.blogappproject.exception.BlogAPIException;
import com.springapplication.blogappproject.exception.ResourceNotFoundException;
import com.springapplication.blogappproject.payload.CommentDto;
import com.springapplication.blogappproject.repository.CommentRepository;
import com.springapplication.blogappproject.repository.PostRepository;
import com.springapplication.blogappproject.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CommentServiceImpl is the implementation of the CommentService interface.
 * It provides methods to handle comment-related operations.
 */
@Service
public class CommentServiceImpl implements CommentService {

    /**
     * Repository for managing Comment entities.
     * Provides methods for performing CRUD operations and custom database queries related to comments.
     * It serves as a data access layer for the CommentService implementation.
     */
    private final CommentRepository commentRepository;
    /**
     * Repository for managing Post entities.
     * Provides methods for performing CRUD operations and custom database queries related to posts.
     * It serves as a data access layer for operations involving posts.
     */
    private final PostRepository postRepository;

    /**
     * An instance of ModelMapper used within the service implementation.
     * Facilitates the mapping between entity objects and Data Transfer Objects (DTOs).
     * Simplifies the process of converting between different object models and helps maintain
     * separation of concerns within the application.
     */
    private ModelMapper modelMapper;
    /**
     * Constructs a new instance of CommentServiceImpl with the required dependencies.
     *
     * @param commentRepository The repository for managing Comment entities. Used to perform
     *                           CRUD operations on comments and interact with the database.
     * @param postRepository The repository for managing Post entities. Used to perform
     *                        operations related to posts and retrieve post data.
     * @param modelMapper The instance of ModelMapper to facilitate mapping between
     *                    entities and DTOs.
     */
    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }
    /**
     * Creates a new comment for a specific post.
     * @param postId The ID of the post to which the comment belongs.
     * @param commentDto The data transfer object containing comment details.
     * @return The created CommentDto object.
     */
    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        // Validate post ID
        if (postId <= 0) {
            throw new IllegalArgumentException("Invalid comment data provided");
        }

        // Validate comment data
        if (commentDto.getName() == null || commentDto.getName().trim().isEmpty() ||
                commentDto.getBody() == null || commentDto.getBody().trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid comment data provided");
        }

        Comment comment = mapToEntity(commentDto);

        // retrieve post entity by id
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));

        // set post to comment entity
        comment.setPost(post);

        // comment entity to DB
        Comment newComment = commentRepository.save(comment);

        return mapToDto(newComment);
    }



    /**
     * Retrieves all comments associated with a specific post ID.
     * @param postId The ID of the post for which comments are to be retrieved.
     * @return A list of CommentDto objects associated with the specified post ID.
     */

    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
        // retrieve comments by postId
        List<Comment> comments = commentRepository.findByPostId(postId);

        // convert list of comment entities to list of comment dto's
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    /**
     * Retrieves a comment by its ID associated with a specific post.
     * @param postId The ID of the post to which the comment belongs.
     * @param commentId The ID of the comment to retrieve.
     * @return The CommentDto object representing the retrieved comment.
     */
    @Override
    public CommentDto getCommentById(long postId, long commentId) {
        Comment comment = retrievePostEntityByID(postId, commentId);

        return mapToDto(comment);
    }

    /**
     * Updates a comment associated with a specific post.
     * @param postId The ID of the post to which the comment belongs.
     * @param commentId The ID of the comment to update.
     * @param commentDto The data transfer object containing updated comment details.
     * @return The updated CommentDto object.
     */

    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentDto) {
        // First retrieve the existing comment and post
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", commentId));

        // Verify the comment belongs to the post
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        // Update the comment fields
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        // The post relationship is maintained as we're updating an existing comment

        Comment updatedComment = commentRepository.save(comment);
        return mapToDto(updatedComment);
    }

    /**
     * Deletes a comment associated with a specific post.
     * @param postId The ID of the post to which the comment belongs.
     * @param commentId The ID of the comment to delete.
     */
    @Override
    public void deleteComment(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", commentId));
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
        commentRepository.delete(comment);
    }


    /**
     * Retrieves a comment entity by its ID associated with a specific post.
     * @param postId The ID of the post to which the comment belongs.
     * @param commentId The ID of the comment to retrieve.
     * @return The Comment entity representing the retrieved comment.
     */
    private Comment retrievePostEntityByID(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post", "id", postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new ResourceNotFoundException("Comment", "id", commentId));
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }
        return comment;
    }

    /**
     * Maps a Comment entity to a CommentDto object.
     *
     * @param comment The Comment entity to be mapped.
     * @return A CommentDto object containing the mapped data from the Comment entity.
     */
    private CommentDto mapToDto(Comment comment) {
        CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
//        commentDto.setId(comment.getId());
//        commentDto.setName(comment.getName());
//        commentDto.setEmail(comment.getEmail());
//        commentDto.setBody(comment.getBody());
        return commentDto;
    }
    /**
     * Maps a CommentDto object to a Comment entity.
     *
     * @param commentDto The CommentDto object to be converted into a Comment entity.
     * @return A Comment entity containing the mapped data from the CommentDto object.
     */
    private Comment mapToEntity(CommentDto commentDto) {
        Comment comment = modelMapper.map(commentDto, Comment.class);
//        comment.setId(commentDto.getId());
//        comment.setName(commentDto.getName());
//        comment.setEmail(commentDto.getEmail());
//        comment.setBody(commentDto.getBody());
        return comment;
    }


}