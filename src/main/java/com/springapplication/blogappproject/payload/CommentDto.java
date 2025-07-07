package com.springapplication.blogappproject.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for the Comment entity.
 * This class is used for transferring comment-related data between application layers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    /* * Unique identifier for the comment.
     *  This field is used to identify the comment in the database.
     *  private long id;
        private String name;
        private String email;
        private String body;
     */

    /**
     * Unique identifier for the comment.
     * This field is used to identify the comment in the database.
     */
    private long id;

    /**
     * Name of the person who made the comment.
     * This field stores the name of the commenter.
     */
    @NotEmpty(message = "Name cannot be empty.")
    private String name;

    /**
     * Email address of the commenter.
     * This field is used for contact or identification purposes.
     */
    @NotEmpty(message = "Email cannot be empty.")
    @Email(message = "Invalid email address.")
    private String email;

    /**
     * Content of the comment.
     * This field contains the actual text of the comment.
     */
    //@NotEmpty(message = "Comment body cannot be empty.")
    //@Size(min = 10, max = 500, message = "Comment body must be between 10 and 500 characters.")
    private String body;

}
