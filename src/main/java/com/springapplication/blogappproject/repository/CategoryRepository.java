package com.springapplication.blogappproject.repository;

import com.springapplication.blogappproject.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Category entities.
 * This interface extends JpaRepository to provide standard CRUD operations
 * and database management functionality for Category entities.
 * It enables the interaction with the database to perform operations such as saving,
 * updating, deleting, and retrieving Category entities.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
