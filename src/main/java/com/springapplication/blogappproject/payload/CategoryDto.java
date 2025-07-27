package com.springapplication.blogappproject.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CategoryDto is a Data Transfer Object used to represent details
 * about a specific category in the application. It is primarily intended
 * for transferring data between different layers, such as the service
 * layer and the presentation layer.
 *
 * This class encapsulates the basic attributes of a category entity,
 * which include the category's ID, name, and description.
 * Instances of this class are typically used in operations
 * such as creating, updating, or retrieving category information.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    /**
     * Represents the unique identifier for the category.
     * This field is used to uniquely identify a specific category within the system.
     */
    private Long id;
    /**
     * Represents the name of the category.
     * This field is used to store the descriptive name of a category
     * in the application. It helps in identifying and displaying
     * category information to the users or other components of the system.
     */
    private String name;
    /**
     * Provides a textual description of the category.
     * This field is used to store additional details or a summary
     * describing the purpose or nature of the category.
     */
    private String description;
}
