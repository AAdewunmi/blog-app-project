package com.springapplication.blogappproject.payload;

import lombok.Data;

/**
 * PostDto is a Data Transfer Object that represents a blog post.
 * It contains fields for the post's ID, title, content, and description.
 * This class is used to transfer data between different layers of the application.
 */
@Data
public class PostDto {

    private  Long id;
    private String title;
    private String content;
    private String description;

    /**
     * Constructor to initialize PostDto with all fields.
     *
     * @param id          the unique identifier of the post
     * @param title       the title of the post
     * @param content     the content of the post
     * @param description a brief description of the post
     */
    public PostDto(Long id, String title, String content, String description) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.description = description;
    }

    /**
     * Default constructor for PostDto.
     * This is used when creating an instance without initializing fields.
     */
    public PostDto() {
        // Default constructor
    }
}
