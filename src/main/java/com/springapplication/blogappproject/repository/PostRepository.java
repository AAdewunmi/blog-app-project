package com.springapplication.blogappproject.repository;

import com.springapplication.blogappproject.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Post entities.
 * This interface extends JpaRepository to provide CRUD operations and custom query methods.
 */

public interface PostRepository extends JpaRepository<Post, Long> {

    // Custom query methods can be defined here if needed
    // For example, to find posts by title:
    // List<Post> findByTitle(String title);
    // Or to find posts containing a specific keyword in the content:
    // List<Post> findByContentContaining(String keyword);
    //@Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments")
    //List<Post> findAllPostsWithComments();

    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.comments c WHERE p.id = :id")
    Optional<Post> findByIdWithComments(@Param("id") Long id);
}