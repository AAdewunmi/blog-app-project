package com.springapplication.blogappproject.payload;

import lombok.Data;

@Data
public class PostDto {

    private  Long id;
    private String title;
    private String content;
    private String description;
}
