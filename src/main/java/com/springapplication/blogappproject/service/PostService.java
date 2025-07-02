package com.springapplication.blogappproject.service;

import com.springapplication.blogappproject.payload.PostDto;
import com.springapplication.blogappproject.payload.PostResponse;

import java.util.List;
/**
 * Service interface for managing blog posts.
 * Provides methods to create, retrieve, update, and delete posts.
 */
public interface PostService {
    /**
     * Creates a new blog post.
     *
     * @param postDto the data transfer object containing post details
     * @return the created PostDto
     */
    PostDto createPost(PostDto postDto);
    /**
     * Retrieves all blog posts with pagination.
     *
     * @param pageNo the page number to retrieve
     * @param pageSize the number of posts per page
     * @return a list of PostDto objects representing the posts
     */
    // ... other methods ...

    // Paginated version
    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
    
    // Non-paginated version
    List<PostDto> getAllPosts();
    /**
     * Retrieves a blog post by its ID.
     *
     * @param id the ID of the post to retrieve
     * @return the PostDto representing the post
     */
    PostDto getPostById(long id);
    /**
     * Updates an existing blog post.
     *
     * @param postDto the data transfer object containing updated post details
     * @param id the ID of the post to update
     * @return the updated PostDto
     */
    PostDto updatePost(PostDto postDto, long id);
    /**
     * Deletes a blog post by its ID.
     *
     * @param id the ID of the post to delete
     */
    void deletePostById(long id);

    
}