package com.springapplication.blogappproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a Role entity in the application.
 * This class is mapped to the "roles" table in the database.
 * A role defines a specific set of permissions or responsibilities assigned to users.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    /**
     * Represents the unique identifier for the Role entity.
     * This field serves as the primary key for the "roles" table in the database.
     * It is auto-generated using the IDENTITY strategy, ensuring unique values for each Role instance.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Name of the role.
     * This field represents the name or designation given to a specific role within the system.
     * It is stored as a string and may be used for role identification and assignment.
     */
    private String name;

}
