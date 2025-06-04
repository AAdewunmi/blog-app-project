package com.springapplication.blogappproject.payload;

import lombok.Data;

@Data
public class PostDto {

    private  Long id;
    private String title;
    private String content;
    private String description;

    public PostDto(Long id, String title, String content, String description) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.description = description;
    }

    public PostDto() {
        // Default constructor
    }
}
