package com.springapplication.blogappproject.controller;

import com.springapplication.blogappproject.payload.PostDto;
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

    public PostController(PostService postService) {
        this.postService = postService;
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
     * Retrieves all blog posts.
     * @return List of PostDto containing all posts.
     */
    @GetMapping
    public List<PostDto> getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ){
        return postService.getAllPosts(pageNo, pageSize);
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
}
