package com.springapplication.blogappproject.controller;

import com.springapplication.blogappproject.payload.CategoryDto;
import com.springapplication.blogappproject.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PreAuthorize("hasRole('USER') and hasRole('ADMIN')")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto){
        CategoryDto savedCategory = categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }
}
