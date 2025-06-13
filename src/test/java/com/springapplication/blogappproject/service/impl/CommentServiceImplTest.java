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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    @MockitoBean
    private CommentRepository commentRepository;

    @MockitoBean
    private PostRepository postRepository;

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


}