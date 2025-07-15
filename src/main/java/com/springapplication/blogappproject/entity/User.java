package com.springapplication.blogappproject.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * Represents a User entity in the application.
 * This class is mapped to the "users" table in the database.
 * A user entity has fields for id, name, username, email, and password.
 * It also establishes a many-to-many relationship with the Role entity.
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")

public class User {

    /**
     * Unique identifier for the User entity.
     * This field is the primary key for the "users" table in the database
     * and is automatically generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Name of the user.
     * This field represents the full name of the user and is stored in plain text.
     */
    private String name;
    /**
     * The username field represents the unique username of the user.
     * This field is mandatory and is marked as non-nullable in the database.
     * Additionally, the username must be unique across all records in the database.
     */
    @Column(nullable = false, unique = true)
    private String username;
    /**
     * Email address of the user.
     * This field is mandatory and must be unique across all users.
     * It is used as a key identifier for the user in the system.
     */
    @Column(nullable = false, unique = true)
    private String email;
    /**
     * The password field of the User entity.
     * This field stores the password of the user.
     * It is a mandatory field and cannot be null.
     * The value is stored as a string.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Represents the roles associated with the user.
     * This field establishes a many-to-many relationship between the User and Role entities.
     * The relationship is defined using a join table named "users_roles".
     * Each entry in the join table maps a user to one or more roles and vice versa.
     * Fetching of roles is eager, which means roles are loaded immediately with the user.
     * Cascade operations are applied to all related entities.
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private Set<Role> roles;

}
