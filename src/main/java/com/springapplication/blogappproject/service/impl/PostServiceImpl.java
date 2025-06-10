package com.springapplication.blogappproject.service.impl;

import com.springapplication.blogappproject.entity.Post;
import com.springapplication.blogappproject.exception.ResourceNotFoundException;
import com.springapplication.blogappproject.payload.PostDto;
import com.springapplication.blogappproject.repository.PostRepository;
import com.springapplication.blogappproject.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/** Implementation of the PostService interface for managing blog posts.
 * Provides methods to create, retrieve, update, and delete posts.
 */
@Service
public class PostServiceImpl implements PostService {

    /** Repository for accessing Post entities.
     * This is injected via constructor injection.
     */
    private final PostRepository postRepository;

    /** Constructor for PostServiceImpl.
     * @param postRepository the repository to be used for Post entities
     */
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    /**
     * Creates a new post.
     * @param postDto the data transfer object containing post details
     * @return the created PostDto
     */
    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = mapToEntity(postDto);
        Post newPost = postRepository.save(post);
        PostDto postResponse = mapToDTO(newPost);
        return postResponse;
    }
    /**
     * Retrieves all posts with pagination.
     * @param pageNo the page number to retrieve
     * @param pageSize the number of posts per page
     * @return a list of PostDto objects representing the posts
     */
    @Override
    public List<PostDto> getAllPosts(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Post> posts = postRepository.findAll(pageable);
        List<Post> listOfPosts = posts.getContent();
        return listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());
    }
    /**
     * Retrieves a post by its ID.
     * @param id the ID of the post to retrieve
     * @return the PostDto representing the post
     */
    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post ", "id: ",  id)
        );
        return mapToDTO(post);
    }
    /**
     * Updates an existing post.
     * @param postDto the data transfer object containing updated post details
     * @param id the ID of the post to update
     * @return the updated PostDto
     */
    @Override
    public PostDto updatePost(PostDto postDto, long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post ", "id: ", id)
        );
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        Post updatedPost = postRepository.save(post);
        return mapToDTO(updatedPost);
    }
    /**
     * Deletes a post by its ID.
     * @param id the ID of the post to delete
     */
    @Override
    public void deletePostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Post ", "id: ", id)
        );
        postRepository.delete(post);
    }

    @Override
    public Object getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    /**
     * Deletes all posts.
     */
    private PostDto mapToDTO(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setDescription(post.getDescription());
        return postDto;
    }
    /**
     * Maps a PostDto to a Post entity.
     * @param postDto the PostDto to map
     * @return the mapped Post entity
     */
    private Post mapToEntity(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setDescription(postDto.getDescription());
        return post;
    }

}
