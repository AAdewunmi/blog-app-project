package com.springapplication.blogappproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents a Comment entity in the application.
 * This class is mapped to the "comments" table in the database.
 * It includes fields for id, name, email, and body.
 * Comments are associated with a specific Post entity, establishing
 * a many-to-one relationship with the Post class.
 */
@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    /**
     * Represents the unique identifier for a comment entity.
     * This field serves as the primary key in the database table.
     * It is auto-generated using the IDENTITY strategy, ensuring uniqueness
     * and automatically managing its value during persistence operations.
     */ /* * Unique identifier for the comment.
     * This is the primary key and is auto-generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * Name of the comment author.
     * This field represents the name of the person who created the comment.
     */
    private String name;
    /**
     * Email address associated with the comment.
     * This field is used to identify the email of the person who posted the comment.
     * It is stored as a string in the database.
     */
    private String email;
    /**
     * The body of the comment.
     * This field contains the textual content of the comment.
     */
    private String body;

    /**
     * Represents the association between a comment and a post.
     * This field establishes a many-to-one relationship between the Comment and Post entities.
     * Each comment must be linked to a single post, whereas a post can have multiple comments.
     * The relationship is managed using a foreign key column "post_id" in the "comments" table.
     * Fetching is configured to be lazy, meaning the associated post is loaded only when explicitly accessed.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    @ToString.Exclude
    private Post post;
}