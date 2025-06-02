package com.springapplication.blogappproject.service;

import com.springapplication.blogappproject.payload.PostDto;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto);

    List<PostDto> getAllPosts();
}
