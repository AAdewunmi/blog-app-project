package com.springapplication.blogappproject.controller;

import com.springapplication.blogappproject.payload.PostDto;
import com.springapplication.blogappproject.payload.PostResponse;
import com.springapplication.blogappproject.repository.PostRepository;
import com.springapplication.blogappproject.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
/**
 * PostController handles HTTP requests related to blog posts.
 * It provides endpoints to create a post, retrieve all posts, and get a post by its ID.
 */
@RestController
@RequestMapping("/api/posts")
@Slf4j
public class PostController {
    /* PostController is a REST controller that manages blog posts.
     * It provides endpoints to create a post, retrieve all posts, and get a post by its ID.
     * The controller uses PostService to handle business logic.
     */
    private PostService postService;
    private PostRepository postRepository;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDto>> getAllPostsList() {
        log.info("Retrieving all posts without pagination");
        List<PostDto> posts = (List<PostDto>) postService.getAllPosts();
        log.info("Retrieved {} posts", posts.size());
        return ResponseEntity.ok(posts);
    }

    @GetMapping
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {
        return postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    }
    /*
     * Creates a new blog post.
     * @param postDto the data transfer object containing post details.
     * @return ResponseEntity containing the created PostDto and HTTP status code.
     */
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    /*
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
    /*
     * Updates a blog post by its ID.
     * @param postDto the data transfer object containing updated post details.
     * @param id the ID of the post to update.
     * @return ResponseEntity containing the updated PostDto and HTTP status code.
     */

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(
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

    /*
     * Deletes a blog post by its ID.
     * @param id the ID of the post to delete.
     * @return ResponseEntity with HTTP status code indicating the result of the deletion.
     */

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
}