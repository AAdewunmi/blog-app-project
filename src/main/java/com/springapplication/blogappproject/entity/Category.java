package com.springapplication.blogappproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents a category entity in the blog application.
 * This class is mapped to the "categories" table in the database.
 * Each category may have multiple associated posts, forming a one-to-many relationship.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    /**
     * Unique identifier for the Category entity.
     * This field serves as the primary key and is auto-generated
     * using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Name of the category.
     * This field represents the textual name used to identify the category in the system.
     * It is stored as a string and is a characteristic attribute of the Category entity.
     */
    private String name;
    /**
     * The description of the category.
     * This field provides additional details about a specific category.
     */
    private String description;

    /**
     * List of posts associated with this category.
     * This field establishes a one-to-many relationship between the Category entity and the Post entity.
     * Each category can be associated with multiple posts.
     * The relationship is bidirectional, with the "category" field in the Post entity as the inverse side.
     * Cascade operations are applied to all associated posts, ensuring that changes to the category
     * are propagated to its posts. Orphan removal is enabled, meaning that any post removed from
     * this list will also be removed from the database.
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;
}
