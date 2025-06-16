package com.springapplication.blogappproject.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * Data Transfer Object for Comment entity.
 * This class is used to transfer comment data between layers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    /* * Unique identifier for the comment.
     * This field is used to identify the comment in the database.
     */
    private long id;

    private String name;
    private String email;
    private String body;

}
