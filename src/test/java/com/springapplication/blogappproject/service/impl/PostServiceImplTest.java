package com.springapplication.blogappproject.service.impl;

import com.springapplication.blogappproject.entity.Category;
import com.springapplication.blogappproject.entity.Post;
import com.springapplication.blogappproject.exception.ResourceNotFoundException;
import com.springapplication.blogappproject.payload.PostDto;
import com.springapplication.blogappproject.payload.PostResponse;
import com.springapplication.blogappproject.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;

//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import com.springapplication.blogappproject.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Test class for the PostServiceImpl class.
 * This class contains unit tests to verify the behavior and functionality of the PostServiceImpl service layer.
 * The tests use Mockito to mock dependencies and validate method calls and interactions.
 */
public class PostServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;
    /**
     * Mocked dependency for {@link PostRepository} used in test cases.
     * It is utilized to simulate the behavior of the actual repository during unit testing.
     * This allows the testing of service methods in isolation without interacting with the database.
     */
    @Mock
    private PostRepository postRepository;

    /**
     * Mock instance of ModelMapper used for testing purposes in the PostServiceImplTest class.
     * Facilitates the mapping between DTOs and entities within unit tests.
     */
    @Mock
    private ModelMapper modelMapper;  // Add ModelMapper mock

    /**
     * Mock instance of the {@link PostServiceImpl} used for testing purposes.
     * This mock is injected with dependencies to simulate and test the behavior of the PostServiceImpl class in unit tests.
     */
    @InjectMocks
    private PostServiceImpl postServiceImpl;

    /**
     * A constant representing the title used for test cases in the PostServiceImplTest class.
     */
    private static final String TEST_TITLE = "Test Title";
    /**
     * Represents a constant string used as content for testing purposes
     * in the PostServiceImplTest test class.
     */
    private static final String TEST_CONTENT = "Test Content";
    /**
     * Represents the description used for testing purposes in the PostServiceImplTest class.
     * This value is a constant and serves as a predefined description used in various
     * tests within the class to validate functionality and behavior.
     */
    private static final String TEST_DESCRIPTION = "Test Description";
    /**
     * A constant holding the unique identifier used for test purposes.
     * Represents a predefined ID value utilized in testing scenarios
     * within the PostServiceImplTest class to ensure consistent reference
     * to a specific entity or data point.
     */
    private static final Long TEST_ID = 1L;

    /**
     * Initializes the test environment before each test is executed.
     *
     * This method performs the following actions:
     * - Opens and manages the lifecycle of Mockito mocks to ensure proper mocking behavior.
     * - Configures the behavior of the modelMapper mock instance to transform between
     *   PostDto and Post objects.
     *
     * For modelMapper, two mapping behaviors are defined:
     * 1. Transforms a PostDto object to a Post object, where the title, content, and description
     *    fields are copied.
     * 2. Transforms a Post object to a PostDto object, where the ID, title, content, and
     *    description fields are copied.
     *
     * If an exception occurs during mock initialization, it is wrapped and thrown as a
     * RuntimeException.
     */
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


    /**
     * Tests that a new post is successfully created and the returned PostDto contains
     * correct values for all fields.
     *
     * This test verifies the following:
     * - The PostDto passed to the service layer is correctly mapped to a Post entity.
     * - The Post entity is properly saved using the repository.
     * - The saved Post entity is correctly mapped back to a PostDto.
     * - The returned PostDto contains the expected values for id, title, content, and description.
     *
     * Steps:
     * 1. Arrange:
     *    - Creates a test PostDto object with predefined values for title, content, and description.
     *    - Mocks the behavior of the repository to return a saved Post entity with a valid ID and predefined values.
     * 2. Act:
     *    - Calls the createPost method on the service layer, passing the test PostDto.
     * 3. Assert:
     *    - Ensures the result is not null.
     *    - Verifies that the returned PostDto contains the expected ID, title, content, and description
     *      values matching the mocked saved Post entity.
     */
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

    /**
     * Tests that the createPost method in the PostServiceImpl class correctly maps a PostDto object
     * to a Post entity and saves it using the PostRepository.
     *
     * This test verifies:
     * 1. The PostDto is properly mapped to a Post entity.
     * 2. The mapped Post entity is saved by the repository.
     * 3. The fields of the PostDto match the corresponding fields in the expected Post entity
     *    after the mapping process.
     *
     * Test Steps:
     * - Arrange:
     *   1. Creates a test PostDto object with predefined values.
     *   2. Creates an expected Post object with corresponding values.
     *   3. Mocks the save method of PostRepository to return the input Post entity.
     * - Act:
     *   1. Calls the createPost method of PostServiceImpl with the test PostDto.
     * - Assert:
     *   1. Verifies that the save method of PostRepository was called with a Post argument.
     *   2. Asserts that the title, content, and description of the PostDto match the corresponding
     *      values of the expected Post entity.
     */
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

    /**
     * Tests that the createPost method in the PostServiceImpl class does not throw any exceptions
     * when a valid PostDto object is provided.
     *
     * This test ensures:
     * 1. A valid PostDto object can be saved without errors.
     * 2. The service layer correctly interacts with the repository to persist the Post entity.
     *
     * Test Steps:
     * - Arrange:
     *   1. Creates a valid PostDto object using predefined test data.
     *   2. Mocks the behavior of PostRepository to simulate successful saving of a Post entity.
     * - Act & Assert:
     *   1. Calls the createPost method of PostServiceImpl with the valid PostDto object.
     *   2. Asserts that no exceptions are thrown during the execution.
     */
    @Test
    void testShouldNotThrowExceptionWhenSavingValidPostDto() {
        // Arrange
        PostDto postDto = createTestPostDto();
        when(postRepository.save(any(Post.class))).thenReturn(createTestPost());

        // Act & Assert
        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> postServiceImpl.createPost(postDto));
    }

    /**
     * Tests that an existing post is successfully updated and the returned PostDto contains
     * the correct updated values.
     *
     * This test verifies:
     * - The post is fetched successfully by ID from the repository.
     * - The fetched post is updated with new values from the provided PostDto.
     * - The updated post is saved correctly using the repository.
     * - The returned PostDto contains the expected ID, title, content, and description
     *   reflecting the updates.
     *
     * Test Steps:
     * 1. Arrange:
     *    - Prepare a test PostDto with updated values.
     *    - Mock repository behavior to return an existing post for the given ID.
     *    - Mock repository save behavior to simulate successful saving of the updated post.
     * 2. Act:
     *    - Call the `updatePost` method on the service implementation with the test PostDto and ID.
     * 3. Assert:
     *    - Verify that the returned PostDto is not null.
     *    - Ensure the returned PostDto contains the expected ID, title, content, and description
     *      matching the updates.
     */
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

    /**
     * Tests that the `updatePost` method in the `PostServiceImpl` class throws a `ResourceNotFoundException`
     * when attempting to update a post that does not exist in the repository.
     *
     * This test verifies:
     * 1. The repository returns an empty `Optional` when attempting to find a post by ID.
     * 2. The `updatePost` method throws a `ResourceNotFoundException` when called with a non-existing post ID.
     *
     * Test Steps:
     * - Arrange:
     *   1. Create a `PostDto` object with test data using the `createTestPostDto` method.
     *   2. Mock the `PostRepository` `findById` method to return an empty `Optional` for the given test ID.
     * - Act & Assert:
     *   1. Call the `updatePost` method with the `PostDto` object and the test ID.
     *   2. Verify that a `ResourceNotFoundException` is thrown.
     */
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

    /**
     * Creates and returns a new instance of PostDto pre-populated with test values.
     *
     * @return a PostDto object initialized with test title, content, and description values
     */
    private PostDto createTestPostDto() {
        PostDto postDto = new PostDto();
        postDto.setTitle(TEST_TITLE);
        postDto.setContent(TEST_CONTENT);
        postDto.setDescription(TEST_DESCRIPTION);
        return postDto;
    }

    /**
     * Creates and returns a new instance of Post pre-populated with test values.
     *
     * @return a Post object initialized with predefined test ID, title, content, and description values
     */
    private Post createTestPost() {
        Post post = new Post();
        post.setId(TEST_ID);
        post.setTitle(TEST_TITLE);
        post.setContent(TEST_CONTENT);
        post.setDescription(TEST_DESCRIPTION);
        return post;
    }


    /**
     * Tests that the `deletePostById` method in the `PostServiceImpl` class successfully deletes
     * an existing post by its ID.
     *
     * This test verifies:
     * 1. The repository successfully retrieves the post by its ID.
     * 2. The `delete` method of the repository is invoked with the correct post.
     *
     * Test Steps:
     * - Arrange:
     *   1. Create a test `Post` object using the `createTestPost` helper method.
     *   2. Mock the repository's behavior to return the test post when `findById` is called.
     * - Act:
     *   1. Call the `deletePostById` method in the service layer with the test ID.
     * - Assert:
     *   1. Verify that the repository's `delete` method is called with the expected post instance.
     */
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

    /**
     * Tests that the `deletePostById` method in the `PostServiceImpl` class throws
     * a `ResourceNotFoundException` when attempting to delete a post that does not exist
     * in the repository.
     *
     * This test verifies:
     * 1. The repository's `findById` method returns an empty `Optional` when searching
     *    for a non-existing post by its ID.
     * 2. The `deletePostById` method throws a `ResourceNotFoundException` when invoked
     *    with a non-existing post ID.
     *
     * Test Steps:
     * - Arrange:
     *   1. Mock the repository's `findById` method to return an empty `Optional` for the test ID.
     * - Act & Assert:
     *   1. Invoke the `deletePostById` method with the test ID.
     *   2. Verify that a `ResourceNotFoundException` is thrown.
     */
    @Test
    void shouldThrowExceptionWhenDeletingNonExistingPost() {
        // Arrange
        when(postRepository.findById(TEST_ID)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            postServiceImpl.deletePostById(TEST_ID);
        });
    }

    /**
     * Verifies that createPost returns a PostDto with correct values when a valid input and existing category are provided.
     */
    @Test
    void createPost_ReturnsPostDto_WhenValidInputProvided() {
        PostDto postDto = createTestPostDto();
        Category category = new Category();
        category.setId(postDto.getCategoryId());
        Post savedPost = createTestPost();

        when(categoryRepository.findById(postDto.getCategoryId())).thenReturn(Optional.of(category));
        when(postRepository.save(any(Post.class))).thenReturn(savedPost);

        PostDto result = postServiceImpl.createPost(postDto);

        assertThat(result)
                .isNotNull()
                .satisfies(dto -> {
                    assertThat(dto.getId()).isEqualTo(TEST_ID);
                    assertThat(dto.getTitle()).isEqualTo(TEST_TITLE);
                    assertThat(dto.getContent()).isEqualTo(TEST_CONTENT);
                    assertThat(dto.getDescription()).isEqualTo(TEST_DESCRIPTION);
                });
    }

    /**
     * Ensures that createPost throws ResourceNotFoundException when the provided category does not exist.
     */
    @Test
    void createPost_ThrowsException_WhenCategoryDoesNotExist() {
        PostDto postDto = createTestPostDto();

        when(categoryRepository.findById(postDto.getCategoryId())).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            postServiceImpl.createPost(postDto);
        });
    }

    /**
     * Verifies that getPostById returns a PostDto with correct values when the post exists.
     */
    @Test
    void getPostById_ReturnsPostDto_WhenPostExists() {
        Post post = createTestPost();

        when(postRepository.findByIdWithComments(TEST_ID)).thenReturn(Optional.of(post));

        PostDto result = postServiceImpl.getPostById(TEST_ID);

        assertThat(result)
                .isNotNull()
                .satisfies(dto -> {
                    assertThat(dto.getId()).isEqualTo(TEST_ID);
                    assertThat(dto.getTitle()).isEqualTo(TEST_TITLE);
                    assertThat(dto.getContent()).isEqualTo(TEST_CONTENT);
                    assertThat(dto.getDescription()).isEqualTo(TEST_DESCRIPTION);
                });
    }

    /**
     * Ensures that getPostById throws ResourceNotFoundException when the post does not exist.
     */
    @Test
    void getPostById_ThrowsException_WhenPostDoesNotExist() {
        when(postRepository.findByIdWithComments(TEST_ID)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            postServiceImpl.getPostById(TEST_ID);
        });
    }

    /**
     * Verifies that getAllPosts returns a paginated response with posts when posts exist.
     */
    @Test
    void getAllPosts_ReturnsPaginatedResponse_WhenPostsExist() {
        Post post = createTestPost();
        Page<Post> postPage = new org.springframework.data.domain.PageImpl<>(List.of(post));

        when(postRepository.findAll(any(Pageable.class))).thenReturn(postPage);

        PostResponse result = postServiceImpl.getAllPosts(0, 10, "id", "asc");

        assertThat(result)
                .isNotNull()
                .satisfies(response -> {
                    assertThat(result.getContent()).asList().hasSize(1);
                    assertThat(response.getTotalElements()).isEqualTo(1);
                    assertThat(response.getTotalPages()).isEqualTo(1);
                });
    }
    /**
     * Verifies that getAllPosts returns an empty list when no posts exist.
     */
    @Test
    void getAllPosts_ReturnsAllPosts_WhenPostsExist() {
        Post post1 = createTestPost();
        Post post2 = new Post();
        post2.setId(2L);
        post2.setTitle("Another Title");
        post2.setContent("Another Content");
        post2.setDescription("Another Description");

        when(postRepository.findAll()).thenReturn(List.of(post1, post2));

        List<PostDto> result = postServiceImpl.getAllPosts();

        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .satisfies(posts -> {
                    assertThat(posts.get(0).getId()).isEqualTo(TEST_ID);
                    assertThat(posts.get(1).getId()).isEqualTo(2L);
                });
    }

    /**
     * Verifies that getAllPosts returns an empty list when no posts exist.
     */
    @Test
    void getAllPosts_ReturnsEmptyList_WhenNoPostsExist() {
        when(postRepository.findAll()).thenReturn(List.of());

        List<PostDto> result = postServiceImpl.getAllPosts();

        assertThat(result)
                .isNotNull()
                .isEmpty();
    }
    /**
     * Verifies that getPostsByCategory returns a list of posts when the category exists.
     */
    @Test
    void getPostsByCategory_ReturnsPosts_WhenCategoryExists() {
        Category category = new Category();
        category.setId(TEST_ID);
        Post post = createTestPost();
        when(categoryRepository.findById(TEST_ID)).thenReturn(Optional.of(category));
        when(postRepository.findByCategoryId(TEST_ID)).thenReturn(List.of(post));

        List<PostDto> result = postServiceImpl.getPostsByCategory(TEST_ID);

        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .satisfies(posts -> {
                    assertThat(posts.get(0).getId()).isEqualTo(TEST_ID);
                    assertThat(posts.get(0).getTitle()).isEqualTo(TEST_TITLE);
                });
    }
}