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
    /**
     * Retrieves a Post entity along with its associated comments based on the provided ID.
     * This method uses a JPQL query with a LEFT JOIN FETCH to eagerly load the comments.
     *
     * @param id The ID of the Post entity to retrieve.
     * @return An Optional containing the Post entity with its comments if found, or an empty Optional otherwise.
     */
    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.comments c WHERE p.id = :id")
    Optional<Post> findByIdWithComments(@Param("id") Long id);
}