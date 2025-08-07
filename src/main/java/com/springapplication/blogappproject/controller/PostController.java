package com.springapplication.blogappproject.controller;

import com.springapplication.blogappproject.payload.PostDto;
import com.springapplication.blogappproject.payload.PostResponse;
import com.springapplication.blogappproject.repository.PostRepository;
import com.springapplication.blogappproject.service.PostService;
import com.springapplication.blogappproject.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
/**
 * PostController handles HTTP requests related to blog posts.
 * It provides endpoints to create a post, retrieve all posts, and get a post by its ID.
 */
@Slf4j
@RestController
@RequestMapping("/api/posts")
public class PostController {
    /**
     * The PostService instance used by the PostController for business logic related to blog posts.
     * This service layer is responsible for handling core operations such as creating, retrieving,
     * updating, and deleting posts, as well as other related functionalities.
     */
    private PostService postService;
    /**
     * The PostRepository instance used for interacting with the database
     * to perform CRUD operations related to blog posts.
     */
    private PostRepository postRepository;

    /**
     * Constructor for PostController.
     *
     * @param postService the service layer object used to manage blog posts.
     */
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Retrieves a paginated list of blog posts based on the provided parameters.
     *
     * @param pageNo the page number to retrieve, defaults to the value defined in {@link AppConstants#DEFAULT_PAGE_NUMBER}.
     * @param pageSize the number of posts per page, defaults to the value defined in {@link AppConstants#DEFAULT_PAGE_SIZE}.
     * @param sortBy the field by which to sort the posts, defaults to the value defined in {@link AppConstants#DEFAULT_SORT_BY}.
     * @param sortDir the sort direction (ascending or descending), defaults to the value defined in {@link AppConstants#DEFAULT_SORT_DIR}.
     * @return a {@link PostResponse} object containing the list of posts and pagination details.
     */
    // Paginated version
    @GetMapping("/paginated")
    public PostResponse getAllPostsPaginated(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIR) String sortDir) {
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }

    /**
     * Retrieves a list of all blog posts.
     *
     * @return ResponseEntity containing a list of PostDto objects representing all blog posts.
     */
    // Non-paginated version
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }
    /**
     * Creates a new blog post.
     *
     * @param postDto the data transfer object containing the details of the new post.
     * @return ResponseEntity containing the created PostDto object and the HTTP status code.
     */ /*
     * Creates a new blog post.
     * @param postDto the data transfer object containing post details.
     * @return ResponseEntity containing the created PostDto and HTTP status code.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    /**
     * Retrieves a blog post by its ID.
     *
     * @param id the ID of the post to retrieve.
     * @return ResponseEntity containing the PostDto if found, or an error response if the post is not found.
     */ /*
     * Retrieves a blog post by its ID.
     * @param id the ID of the post to retrieve.
     * @return ResponseEntity containing the PostDto if found, or an error if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") long id) {
        log.info("Receiving request to find post with id: {}", id);
        try {
            PostDto post = postService.getPostById(id);
            log.info("Found post with id: {}", id);
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            log.error("Error finding post with id: {}", id, e);
            throw e;
        }
    }
    /**
     * Updates a blog post by its ID.
     *
     * @param postDto the data transfer object containing updated post details
     * @param id the ID of the post to update
     * @return ResponseEntity containing the updated PostDto and HTTP status code
     */ /*
     * Updates a blog post by its ID.
     * @param postDto the data transfer object containing updated post details.
     * @param id the ID of the post to update.
     * @return ResponseEntity containing the updated PostDto and HTTP status code.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(
            @Valid
            @RequestBody PostDto postDto,
            @PathVariable(name = "id") long id) {
        log.info("Receiving request to update post with id: {}", id);
        try {
            PostDto updatedPost = postService.updatePost(postDto, id);
            log.info("Updated post with id: {}", id);
            return new ResponseEntity<>(updatedPost, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating post with id: {}", id, e);
            throw e;
        }
    }

    /**
     * Deletes a blog post by its ID.
     *
     * @param id the ID of the post to delete
     * @return ResponseEntity with HTTP status code indicating the result of the deletion
     */ /*
     * Deletes a blog post by its ID.
     * @param id the ID of the post to delete.
     * @return ResponseEntity with HTTP status code indicating the result of the deletion.
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostById(@PathVariable(name = "id") long id) {
        log.info("Receiving request to delete post with id: {}", id);
        try {
            postService.deletePostById(id);
            log.info("Deleted post with id: {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error deleting post with id: {}", id, e);
            throw e;
        }
    }
    /**
     * Tests the connection to the database by checking if a post with the specified ID exists.
     *
     * @param id the ID of the post to check in the database
     * @return a ResponseEntity containing a string message indicating whether the post exists,
     *         or an error message in case of an exception
     */
    @GetMapping("/test/{id}")
    public ResponseEntity<String> testDatabaseConnection(@PathVariable(name = "id") long id) {
        try {
            boolean exists = postRepository.existsById(id);
            return ResponseEntity.ok("Post with ID " + id + " exists: " + exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error checking post: " + e.getMessage());
        }
    }
    @GetMapping("/category/{id}")
    public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable("id") Long categoryId) {
        List<PostDto> postDtos = postService.getPostsByCategory(categoryId);
        return ResponseEntity.ok(postDtos);
    }
}