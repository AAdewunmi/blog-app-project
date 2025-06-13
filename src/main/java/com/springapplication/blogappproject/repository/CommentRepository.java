package com.springapplication.blogappproject.repository;

import com.springapplication.blogappproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing Comment entities.
 * This interface extends JpaRepository to provide CRUD operations for Comment entities.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /**
     * Finds all comments associated with a specific post ID.
     *
     * @param postId the ID of the post for which comments are to be retrieved
     * @return a list of comments associated with the specified post ID
     */
    List<Comment> findByPostId(Long postId);

}
