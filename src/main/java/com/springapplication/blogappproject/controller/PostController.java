package com.springapplication.blogappproject.controller;

import com.springapplication.blogappproject.payload.PostDto;
import com.springapplication.blogappproject.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@Slf4j
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        return new ResponseEntity<>(postService.createPost(postDto), HttpStatus.CREATED);
    }

    @GetMapping
    public List<PostDto> getAllPosts(){
        return postService.getAllPosts();
    }

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


}
