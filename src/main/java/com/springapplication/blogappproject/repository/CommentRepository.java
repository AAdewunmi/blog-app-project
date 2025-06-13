package com.springapplication.blogappproject.repository;

import com.springapplication.blogappproject.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Comment entities.
 * This interface extends JpaRepository to provide CRUD operations for Comment entities.
 */
public class CommentRepository extends JpaRepository<Comment, Long> {


}
