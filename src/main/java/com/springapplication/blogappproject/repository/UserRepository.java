package com.springapplication.blogappproject.repository;

import com.springapplication.blogappproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository interface for managing User entities.
 * This interface extends JpaRepository to provide standard CRUD operations
 * and includes custom query methods for handling User-specific operations.
 *
 * Methods in this interface allow for finding users by email, username, or a combination
 * of username and email. Additionally, it provides methods to check the existence of users
 * based on their username or email.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves the user associated with the specified email address.
     *
     * @param email the email address of the user to be retrieved
     * @return an Optional containing the User entity if found, otherwise an empty Optional
     */
    Optional<User> findByEmail(String email);

    /**
     * Retrieves a user entity based on the provided username or email address.
     * If a user exists with either the specified username or email, it will be returned as an Optional.
     *
     * @param username the username to search for
     * @param email the email address to search for
     * @return an Optional containing the User entity if found; otherwise, an empty Optional
     */
    Optional<User> findByUsernameOrEmail(String username, String email);

    /**
     * Retrieves a User entity wrapped in an Optional by the specified username.
     *
     * @param username the unique username of the user to be retrieved; must not be null
     * @return an Optional containing the User if found, or an empty Optional if no user exists with the provided username
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks whether a user with the specified username exists in the system.
     *
     * @param username the username to check for existence
     * @return true if a user with the given username exists, false otherwise
     */
    Boolean existsByUsername(String username);

    /**
     * Checks if a user with the specified email address exists in the database.
     *
     * @param email the email address to check for existence
     * @return true if a user with the given email exists, false otherwise
     */
    Boolean existsByEmail(String email);


}
