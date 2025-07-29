package com.springapplication.blogappproject.service.impl;

import com.springapplication.blogappproject.entity.Category;
import com.springapplication.blogappproject.payload.CategoryDto;
import com.springapplication.blogappproject.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for {@code CategoryServiceImpl}.
 * This class is responsible for unit testing the methods in the {@code CategoryServiceImpl} class,
 * specifically focusing on the `addCategory` method to ensure correct functionality.
 * Mocks are used to simulate the behavior of dependencies such as {@code CategoryRepository}
 * and {@code ModelMapper}.
 */
@SpringBootTest
public class CategoryServiceImplTest {

    /**
     * Mocked instance of the {@code CategoryRepository} interface.
     * Used in unit tests to simulate the behavior of the {@code CategoryRepository}
     * without requiring a connection to the database. This is particularly useful
     * for isolating and testing the logic within the {@code CategoryServiceImpl}.
     */
    @Mock
    private CategoryRepository categoryRepository;

    /**
     * Mock instance of {@code ModelMapper} used for mapping between DTOs and entities
     * in the unit tests for {@code CategoryServiceImpl}.
     * The mock allows for controlled behavior and validation of interactions during testing.
     */
    @Mock
    private ModelMapper modelMapper;

    /**
     * An instance of {@code CategoryServiceImpl} used for testing.
     * This field is annotated with {@code @InjectMocks} to enable automatic injection
     * of mock dependencies into the {@code CategoryServiceImpl} instance, facilitating unit testing.
     * The dependencies, such as {@code CategoryRepository} and {@code ModelMapper},
     * are mocked and injected into this instance to simulate the behavior of the actual service class
     * without interacting with external systems or the database.
     */
    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    /**
     * Tests the functionality of adding a valid category using the addCategory method
     * in the CategoryServiceImpl service.
     *
     * This test validates the following workflow:
     * 1. Maps the input CategoryDto to a Category entity.
     * 2. Saves the Category entity in the repository.
     * 3. Maps the saved Category entity back to a CategoryDto.
     * 4. Ensures the returned CategoryDto is equivalent to the input data after saving.
     *
     * The test asserts that the expected data is saved and correctly returned.
     * It also verifies interactions with dependencies, including the model mapper
     * and the category repository.
     *
     * Assertions:
     * - The ID, name, and description of the returned CategoryDto match the saved data.
     *
     * Mocks:
     * - Maps the input CategoryDto to a Category entity using the modelMapper.
     * - Persists the Category entity using the categoryRepository.
     * - Maps the saved Category entity back to a CategoryDto using the modelMapper.
     *
     * Verifications:
     * - Confirms the modelMapper is used to map the input and saved entities.
     * - Verifies the categoryRepository's save method is called with the mapped entity.
     */

    @Test
    void testAddCategory_Valid_CategoryDto_Returns_Saved_CategoryDto() {
        // Arrange
        CategoryDto inputDto = new CategoryDto();
        inputDto.setId(1L);
        inputDto.setName("Technology");
        inputDto.setDescription("All about tech");

        Category categoryEntity = new Category();
        categoryEntity.setId(1L);
        categoryEntity.setName("Technology");
        categoryEntity.setDescription("All about tech");

        Category savedCategoryEntity = new Category();
        savedCategoryEntity.setId(1L);
        savedCategoryEntity.setName("Technology");
        savedCategoryEntity.setDescription("All about tech");

        CategoryDto savedDto = new CategoryDto();
        savedDto.setId(1L);
        savedDto.setName("Technology");
        savedDto.setDescription("All about tech");

        when(modelMapper.map(inputDto, Category.class)).thenReturn(categoryEntity);
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategoryEntity);
        when(modelMapper.map(savedCategoryEntity, CategoryDto.class)).thenReturn(savedDto);

        // Act
        CategoryDto result = categoryServiceImpl.addCategory(inputDto);

        // Assert
        assertEquals(savedDto.getId(), result.getId());
        assertEquals(savedDto.getName(), result.getName());
        assertEquals(savedDto.getDescription(), result.getDescription());
        verify(modelMapper).map(inputDto, Category.class);
        verify(categoryRepository).save(categoryEntity);
        verify(modelMapper).map(savedCategoryEntity, CategoryDto.class);
    }
}