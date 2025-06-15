package com.springapplication.blogappproject.controller;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        commentDto = new CommentDto();
        commentDto.setBody("Test comment");
        commentDto.setEmail("test@example.com");
        commentDto.setName("Test User");
    }

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


}