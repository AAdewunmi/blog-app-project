package com.springapplication.blogappproject.service.impl;

import com.springapplication.blogappproject.entity.Category;
import com.springapplication.blogappproject.exception.ResourceNotFoundException;
import com.springapplication.blogappproject.payload.CategoryDto;
import com.springapplication.blogappproject.repository.CategoryRepository;
import com.springapplication.blogappproject.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link CategoryService} interface that provides
 * business logic for managing categories in the application.
 * This class interacts with the data persistence layer through the {@link CategoryRepository}
 * and uses {@link ModelMapper} to map between entity and DTO objects.
 * It is annotated with {@code @Service}, making it eligible for Spring's component scanning
 * to be automatically detected and registered as a spring bean.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    /**
     * Repository interface for performing database operations on Category entities.
     * Provides CRUD functionality and interacts with the persistence layer to manage categories.
     */
    private CategoryRepository categoryRepository;
    /**
     * Instance of {@link ModelMapper} utilized for mapping objects between
     * DTOs (Data Transfer Objects) and entity classes. This field facilitates
     * the conversion of objects, reducing boilerplate code and ensuring a
     * clear separation between different layers of the application. It improves
     * maintainability and readability in operations that require object transformation,
     * such as mapping data from a database entity to an API response DTO.
     */
    private ModelMapper modelMapper;

    /**
     * Constructs a new instance of the {@code CategoryServiceImpl}.
     * This constructor initializes the service with the necessary dependencies for
     * interacting with the persistence layer and performing object mappings.
     *
     * @param categoryRepository the repository used to perform database operations for Category entities
     * @param modelMapper the mapper used to map between Category entities and DTOs
     */
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Adds a new category to the system.
     * The method accepts a CategoryDto object, converts it into a Category entity,
     * persists it into the database, and returns the saved entity as a CategoryDto.
     *
     * @param categoryDto the data transfer object containing the category details
     *                    to be added. It contains attributes like name and description.
     * @return the persisted category details mapped to a CategoryDto object
     */
    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }
    /**
     * Retrieves a category by its ID.
     * If the category is not found, a {@link ResourceNotFoundException} is thrown.
     *
     * @param categoryId the ID of the category to retrieve
     * @return the category details mapped to a {@link CategoryDto} object
     * @throws ResourceNotFoundException if no category is found with the given ID
     */
    @Override
    public CategoryDto getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category ", "id ", categoryId));
        return modelMapper.map(category, CategoryDto.class);
    }
    /**
     * Retrieves all categories from the system.
     * Currently, this method returns an empty list.
     *
     * @return a list of {@link CategoryDto} objects representing all categories
     */
    @Override
    public List<CategoryDto> getAllCategories() {
        return List.of();
    }
    /**
     * Updates an existing category with new details.
     * This method is not yet implemented and currently returns null.
     *
     * @param categoryDto the data transfer object containing the updated category details
     * @param categoryId the ID of the category to update
     * @return the updated category details mapped to a {@link CategoryDto} object
     */
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {
        return null;
    }
    /**
     * Deletes a category by its ID.
     * This method is not yet implemented and currently performs no action.
     *
     * @param categoryId the ID of the category to delete
     */
    @Override
    public void deleteCategory(Long categoryId) {

    }
}
