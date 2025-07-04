package com.springapplication.blogappproject.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * PostDto is a Data Transfer Object that represents a blog post.
 * It contains fields for the post's ID, title, content, and description.
 * This class is used to transfer data between different layers of the application.
 */
@Data
public class PostDto {

    /**
     * Represents the unique identifier for a post.
     * This field is used to uniquely identify each post within the system.
     */
    private  Long id;
    /**
     * The title of the blog post.
     * This field represents the main heading or name of the blog post.
     */
    @NotEmpty(message = "Title cannot be empty.")
    @Size(min = 5, max = 255, message = "Title must be between 5 and 255 characters.")
    private String title;
    /**
     * Represents the main textual content of a blog post.
     * This field is used to store the full body of the post as a string.
     */
    private String content;
    /**
     * A brief description of the blog post.
     * This field provides a summary or overview of the post's content.
     */
    @NotEmpty(message = "Description cannot be empty.")
    private String description;

    /**
     * Represents a collection of comments associated with a blog post.
     * This field stores multiple comments related to a specific post.
     */
    private Set<CommentDto> comments;

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
        this.comments = new HashSet<>();  // Initialize in constructor too

    }

    /**
     * Default constructor for PostDto.
     * This is used when creating an instance without initializing fields.
     */
    public PostDto() {
        this.comments = new HashSet<>();  // Initialize in default constructor

    }
}
