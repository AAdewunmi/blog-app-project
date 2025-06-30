package com.springapplication.blogappproject.service.impl;

import com.springapplication.blogappproject.entity.Post;
import com.springapplication.blogappproject.exception.ResourceNotFoundException;
import com.springapplication.blogappproject.payload.PostDto;
import com.springapplication.blogappproject.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private ModelMapper modelMapper;  // Add ModelMapper mock

    @InjectMocks
    private PostServiceImpl postServiceImpl;

    private static final String TEST_TITLE = "Test Title";
    private static final String TEST_CONTENT = "Test Content";
    private static final String TEST_DESCRIPTION = "Test Description";
    private static final Long TEST_ID = 1L;

    @BeforeEach
    void setUp() {
        try (var mocks = MockitoAnnotations.openMocks(this)) {
            // Set up modelMapper mock behavior
            when(modelMapper.map(any(PostDto.class), any())).thenAnswer(invocation -> {
                PostDto source = invocation.getArgument(0);
                Post target = new Post();
                target.setTitle(source.getTitle());
                target.setContent(source.getContent());
                target.setDescription(source.getDescription());
                return target;
            });

            when(modelMapper.map(any(Post.class), any())).thenAnswer(invocation -> {
                Post source = invocation.getArgument(0);
                PostDto target = new PostDto();
                target.setId(source.getId());
                target.setTitle(source.getTitle());
                target.setContent(source.getContent());
                target.setDescription(source.getDescription());
                return target;
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to open mocks", e);
        }
    }


    @Test
    void shouldCreatePostAndReturnPostDtoWithCorrectValues() {
        // Arrange
        PostDto postDto = createTestPostDto();
        Post savedPost = createTestPost();
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        // Act
        PostDto result = postServiceImpl.createPost(postDto);

        // Assert
        assertThat(result)
                .isNotNull()
                .satisfies(dto -> {
                    assertThat(dto.getId()).isEqualTo(TEST_ID);
                    assertThat(dto.getTitle()).isEqualTo(TEST_TITLE);
                    assertThat(dto.getContent()).isEqualTo(TEST_CONTENT);
                    assertThat(dto.getDescription()).isEqualTo(TEST_DESCRIPTION);
                });
    }

    @Test
    void testShouldMapPostDtoToPostAndSaveWhenCreatePostIsCalled() {
        // Arrange
        PostDto postDto = createTestPostDto();
        Post expectedPost = createTestPost();
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        postServiceImpl.createPost(postDto);

        // Assert
        org.mockito.Mockito.verify(postRepository).save(any(Post.class));
        assertThat(postDto.getTitle()).isEqualTo(expectedPost.getTitle());
        assertThat(postDto.getContent()).isEqualTo(expectedPost.getContent());
        assertThat(postDto.getDescription()).isEqualTo(expectedPost.getDescription());
    }



    @Test
    void shouldUpdatePostSuccessfully() {
        // Arrange
        PostDto postDto = createTestPostDto();
        Post existingPost = createTestPost();
        when(postRepository.findById(TEST_ID)).thenReturn(java.util.Optional.of(existingPost));
        when(postRepository.save(any(Post.class))).thenReturn(existingPost);

        // Act
        PostDto result = postServiceImpl.updatePost(postDto, TEST_ID);

        // Assert
        assertThat(result)
                .isNotNull()
                .satisfies(dto -> {
                    assertThat(dto.getId()).isEqualTo(TEST_ID);
                    assertThat(dto.getTitle()).isEqualTo(TEST_TITLE);
                    assertThat(dto.getContent()).isEqualTo(TEST_CONTENT);
                    assertThat(dto.getDescription()).isEqualTo(TEST_DESCRIPTION);
                });
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingPost() {
        // Arrange
        PostDto postDto = createTestPostDto();
        when(postRepository.findById(TEST_ID)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            postServiceImpl.updatePost(postDto, TEST_ID);
        });
    }

    private PostDto createTestPostDto() {
        PostDto postDto = new PostDto();
        postDto.setTitle(TEST_TITLE);
        postDto.setContent(TEST_CONTENT);
        postDto.setDescription(TEST_DESCRIPTION);
        return postDto;
    }

    private Post createTestPost() {
        Post post = new Post();
        post.setId(TEST_ID);
        post.setTitle(TEST_TITLE);
        post.setContent(TEST_CONTENT);
        post.setDescription(TEST_DESCRIPTION);
        return post;
    }


    @Test
    void shouldDeletePostByIdSuccessfully() {
        // Arrange
        Post existingPost = createTestPost();
        when(postRepository.findById(TEST_ID)).thenReturn(java.util.Optional.of(existingPost));

        // Act
        postServiceImpl.deletePostById(TEST_ID);

        // Assert
        org.mockito.Mockito.verify(postRepository).delete(existingPost);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingPost() {
        // Arrange
        when(postRepository.findById(TEST_ID)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            postServiceImpl.deletePostById(TEST_ID);
        });
    }
}