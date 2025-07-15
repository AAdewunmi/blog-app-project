package com.springapplication.blogappproject.repository;

import com.springapplication.blogappproject.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing Role entities.
 * Extends JpaRepository to inherit basic CRUD operations and
 * provides custom query methods related to Role entity operations.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a role by its name.
     *
     * @param name the name of the role to find
     * @return an Optional containing the Role if found, otherwise an empty Optional
     */
    Optional<Role> findByName(String name);

    /**
     * Checks if a role with the specified name exists.
     *
     * @param name the name of the role to check
     * @return true if a role with the given name exists, false otherwise
     */
    // Boolean existsByName(String name);
}
