package com.springapplication.blogappproject.controller;
import com.springapplication.blogappproject.payload.CategoryDto;
import com.springapplication.blogappproject.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    /**
     * A service layer dependency used to handle business logic
     * related to category management. This includes operations
     * such as creating, retrieving, updating, and deleting categories.
     * The CategoryService interface defines the methods that
     * this service must implement.
     */
    private CategoryService categoryService;
    private CategoryDto categoryDto;
    /**
     * Constructs an instance of CategoryController with the provided CategoryService.
     * The CategoryService is used to handle business logic related to category
     * operations such as creating, retrieving, updating, and deleting categories.
     *
     * @param categoryService the service responsible for handling category-related operations
     */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Adds a new category to the system. This method is accessible only
     * to users with the "ADMIN" role. It accepts a {@link CategoryDto} object
     * containing details of the category to be created and returns the
     * newly created category details with its assigned ID and other properties.
     *
     * @param categoryDto the data transfer object containing the details of the category to be added,
     *                    such as name and description
     * @return a ResponseEntity containing the newly created {@link CategoryDto} object and an HTTP status of CREATED
     */

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto savedCategory = categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }
    /**
     * Retrieves a category by its ID. This method returns the details of the
     * specified category if it exists. If the category is not found, it will
     * return a 404 Not Found status.
     *
     * @param categoryId the ID of the category to retrieve
     * @return a ResponseEntity containing the {@link CategoryDto} object with the category details,
     *         or a 404 Not Found status if the category does not exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable("id") Long categoryId) {
        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        return ResponseEntity.ok(categoryDto);
    }

    /**
     * Retrieves all categories from the system. This method returns a list of
     * all categories available in the application.
     *
     * @return a ResponseEntity containing a list of {@link CategoryDto} objects representing all categories
     */
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    /**
     * Updates an existing category with the provided details. This method
     * accepts a {@link CategoryDto} object containing the updated details
     * and the ID of the category to update. It returns the updated category details.
     *
     * @param categoryDto the data transfer object containing the updated details of the category
     * @param categoryId  the ID of the category to update
     * @return a ResponseEntity containing the updated {@link CategoryDto} object
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto categoryDto,
                                                      @PathVariable("id") Long categoryId) {
        return ResponseEntity.ok(categoryService.updateCategory(categoryDto, categoryId));
    }

    /**
     * Deletes a category based on the provided category ID. This method removes
     * the category associated with the given ID from the system. If the category
     * does not exist, it will return a 404 Not Found status.
     *
     * @param categoryId the ID of the category to be deleted
     * @return a ResponseEntity with an HTTP status of NO_CONTENT if the deletion is successful
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCategory(@PathVariable("id") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("Category deleted successfully!");
    }
}
