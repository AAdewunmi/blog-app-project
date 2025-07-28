package com.springapplication.blogappproject.service;

import com.springapplication.blogappproject.payload.CategoryDto;

import java.util.List;

/**
 * Service interface for managing categories in the application.
 * Provides methods to perform CRUD operations on category entities.
 */
public interface CategoryService {

    /**
     * Adds a new category to the system.
     * The provided categoryDto object contains the necessary details
     * for creating a new category, such as its name and description.
     *
     * @param categoryDto the data transfer object containing details of the category to be added
     * @return the newly created CategoryDto object with its assigned ID and other details
     */
    CategoryDto addCategory(CategoryDto categoryDto);

    /**
     * Retrieves the category details based on the provided category ID.
     *
     * @param categoryId the ID of the category to retrieve
     * @return a {@code CategoryDto} containing the details of the specified category
     */
    CategoryDto getCategory(Long categoryId);

    /**
     * Retrieves a list of all categories in the application.
     * This method is designed to fetch all category data and return it as a list of {@code CategoryDto} objects.
     *
     * @return a list of {@code CategoryDto} objects representing all categories
     */
    List<CategoryDto> getAllCategories();

    /**
     * Updates an existing category with the provided details.
     * Accepts a CategoryDto object containing updated category information and the ID of the category to be updated.
     *
     * @param categoryDto the data transfer object containing the updated details of the category
     * @param categoryId the ID of the category to be updated
     * @return the updated CategoryDto object representing the category with the updated information
     */
    CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId);

    /**
     * Deletes a category based on the provided category ID.
     * This method removes the category associated with the given ID
     * from the system. If the category does not exist, an appropriate
     * exception may be thrown.
     *
     * @param categoryId the ID of the category to be deleted
     */
    void deleteCategory(Long categoryId);

}
