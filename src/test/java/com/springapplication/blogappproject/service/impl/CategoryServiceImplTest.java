package com.springapplication.blogappproject.service.impl;

import com.springapplication.blogappproject.entity.Category;
import com.springapplication.blogappproject.payload.CategoryDto;
import com.springapplication.blogappproject.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.List;
import com.springapplication.blogappproject.exception.ResourceNotFoundException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    /**
     * Tests the functionality of adding a null category using the addCategory method
     * in the CategoryServiceImpl service.
     *
     * This test validates that an IllegalArgumentException is thrown when a null
     * CategoryDto is passed to the addCategory method.
     *
     * The test asserts that the exception is thrown as expected, ensuring that
     * the service correctly handles null inputs.
     *
     * Mocks:
     * - None, as this test focuses on exception handling for null input.
     *
     * Verifications:
     * - Confirms that an IllegalArgumentException is thrown when null is passed.
     */
    @Test
    void testAddCategory_NullCategoryDto_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> categoryServiceImpl.addCategory(null));
    }

    /**
     * Tests the functionality of retrieving a category by its ID using the getCategory method
     * in the CategoryServiceImpl service.
     *
     * This test validates the following workflow:
     * 1. Retrieves a Category entity from the repository based on the provided ID.
     * 2. Maps the retrieved Category entity to a CategoryDto.
     * 3. Ensures the returned CategoryDto contains the expected data.
     *
     * The test asserts that the ID, name, and description of the returned CategoryDto
     * match those of the retrieved Category entity.
     *
     * Mocks:
     * - Retrieves a Category entity from the categoryRepository based on the provided ID.
     * - Maps the retrieved Category entity to a CategoryDto using modelMapper.
     *
     * Verifications:
     * - Confirms that the modelMapper is used to map the retrieved entity to a DTO.
     */
    @Test
    void testGetCategory_ValidId_ReturnsCategoryDto() {
        // Arrange
        Category category = new Category();
        category.setId(1L);
        category.setName("Technology");
        category.setDescription("All about tech");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(modelMapper.map(category, CategoryDto.class)).thenReturn(new CategoryDto(1L, "Technology", "All about tech"));

        // Act
        CategoryDto result = categoryServiceImpl.getCategory(1L);

        // Assert
        assertEquals(1L, result.getId());
        assertEquals("Technology", result.getName());
        assertEquals("All about tech", result.getDescription());
    }
    /**
     * Tests the functionality of retrieving a category by an invalid ID using the getCategory method
     * in the CategoryServiceImpl service.
     *
     * This test validates that a ResourceNotFoundException is thrown when an invalid ID
     * (not present in the repository) is passed to the getCategory method.
     *
     * The test asserts that the exception is thrown as expected, ensuring that
     * the service correctly handles cases where the requested category does not exist.
     *
     * Mocks:
     * - Simulates the behavior of the categoryRepository to return an empty Optional for an invalid ID.
     *
     * Verifications:
     * - Confirms that a ResourceNotFoundException is thrown when an invalid ID is requested.
     */
    @Test
    void testGetCategory_InvalidId_ThrowsResourceNotFoundException() {
        // Arrange
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.getCategory(99L));
    }
    /**
     * Tests the functionality of retrieving all categories using the getAllCategories method
     * in the CategoryServiceImpl service.
     *
     * This test validates that the method returns an empty list when there are no categories
     * present in the repository.
     *
     * The test asserts that the returned list is empty, ensuring that the service correctly
     * handles cases where no categories exist.
     *
     * Mocks:
     * - Simulates the behavior of the categoryRepository to return an empty list when findAll is called.
     *
     * Verifications:
     * - Confirms that the returned list from getAllCategories is empty.
     */
    @Test
    void testGetAllCategories_NoCategories_ReturnsEmptyList() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(List.of());

        // Act
        List<CategoryDto> result = categoryServiceImpl.getAllCategories();

        // Assert
        assertEquals(0, result.size());
    }
    /**
     * Tests the functionality of updating a category using the updateCategory method
     * in the CategoryServiceImpl service.
     *
     * This test validates that the method correctly updates an existing category
     * when a valid CategoryDto and ID are provided.
     *
     * The test asserts that the returned CategoryDto contains the updated data,
     * ensuring that the service correctly handles update requests for existing categories.
     *
     * Mocks:
     * - Simulates the behavior of the categoryRepository to return an existing Category entity.
     * - Maps the updated Category entity back to a CategoryDto using modelMapper.
     *
     * Verifications:
     * - Confirms that the modelMapper is used to map the updated entity to a DTO.
     */
    @Test
    void testUpdateCategory_NullCategoryDto_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> categoryServiceImpl.updateCategory(null, 1L));
    }
    /**
     * Tests the functionality of deleting a category by a valid ID using the deleteCategory method
     * in the CategoryServiceImpl service.
     *
     * This test validates that the method performs the deletion operation when a valid ID
     * (present in the repository) is passed to the deleteCategory method.
     *
     * The test asserts that the deleteById method of the categoryRepository is called with the correct ID,
     * ensuring that the service correctly handles deletion requests for existing categories.
     *
     * Mocks:
     * - Simulates the behavior of the categoryRepository to return true for existsById on a valid ID.
     *
     * Verifications:
     * - Confirms that deleteById is called with the correct ID when a valid ID is requested for deletion.
     */
    @Test
    void testDeleteCategory_ValidId_PerformsDeletion() {
        // Arrange
        when(categoryRepository.existsById(1L)).thenReturn(true);

        // Act
        categoryServiceImpl.deleteCategory(1L);

        // Assert
        verify(categoryRepository).deleteById(1L);
    }
    /**
     * Tests the functionality of deleting a category by an invalid ID using the deleteCategory method
     * in the CategoryServiceImpl service.
     *
     * This test validates that a ResourceNotFoundException is thrown when an invalid ID
     * (not present in the repository) is passed to the deleteCategory method.
     *
     * The test asserts that the exception is thrown as expected, ensuring that
     * the service correctly handles cases where the requested category does not exist.
     *
     * Mocks:
     * - Simulates the behavior of the categoryRepository to return false for existsById on an invalid ID.
     *
     * Verifications:
     * - Confirms that a ResourceNotFoundException is thrown when an invalid ID is requested for deletion.
     */
    @Test
    void testDeleteCategory_InvalidId_ThrowsResourceNotFoundException() {
        // Arrange
        when(categoryRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.deleteCategory(99L));
    }

    /**
     * Validates that adding a category with an empty name throws an IllegalArgumentException.
     * Ensures the service enforces non-empty category names.
     */
    @Test
    void testAddCategory_EmptyName_ThrowsException() {
        // Arrange
        CategoryDto inputDto = new CategoryDto();
        inputDto.setId(1L);
        inputDto.setName("");
        inputDto.setDescription("Description");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> categoryServiceImpl.addCategory(inputDto));
    }

    /**
     * Validates that retrieving a category with a null ID throws an IllegalArgumentException.
     * Ensures the service enforces non-null IDs for retrieval.
     */
    @Test
    void getCategory_NullId_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> categoryServiceImpl.getCategory(null));
    }

    /**
     * Validates that retrieving all categories returns a list containing all categories.
     * Ensures the service correctly maps and returns multiple categories.
     */
    @Test
    void getAllCategories_MultipleCategories_ReturnsList() {
        // Arrange
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Tech");
        category1.setDescription("Tech description");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Health");
        category2.setDescription("Health description");

        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        // Act
        List<CategoryDto> result = categoryServiceImpl.getAllCategories();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Tech", result.get(0).getName());
        assertEquals("Health", result.get(1).getName());
    }

    /**
     * Validates that updating a category with valid data returns the updated CategoryDto.
     * Ensures the service correctly updates and maps the category.
     */
    @Test
    void updateCategory_ValidCategoryDto_ReturnsUpdatedCategoryDto() {
        // Arrange
        Category existingCategory = new Category();
        existingCategory.setId(1L);
        existingCategory.setName("Old Name");
        existingCategory.setDescription("Old Description");

        CategoryDto updatedDto = new CategoryDto();
        updatedDto.setId(1L);
        updatedDto.setName("New Name");
        updatedDto.setDescription("New Description");

        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName("New Name");
        updatedCategory.setDescription("New Description");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);
        when(modelMapper.map(updatedCategory, CategoryDto.class)).thenReturn(updatedDto);

        // Act
        CategoryDto result = categoryServiceImpl.updateCategory(updatedDto, 1L);

        // Assert
        assertEquals("New Name", result.getName());
        assertEquals("New Description", result.getDescription());
    }

    /**
     * Validates that deleting a category with a null ID throws an IllegalArgumentException.
     * Ensures the service enforces non-null IDs for deletion.
     */
    @Test
    void deleteCategory_NullId_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> categoryServiceImpl.deleteCategory(null));
    }

    /**
     * Tests that deleting a category with a valid ID calls the repository's deleteById method.
     * Ensures the service performs the deletion operation for existing categories.
     */
    @Test
    void deleteCategory_ValidId_DeletesCategorySuccessfully() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        categoryServiceImpl.deleteCategory(1L);

        verify(categoryRepository).deleteById(1L);
    }

    /**
     * Tests that deleting a category with an invalid ID throws a ResourceNotFoundException.
     * Ensures the service handles deletion requests for non-existent categories correctly.
     */
    @Test
    void deleteCategory_InvalidId_ThrowsResourceNotFoundException() {
        when(categoryRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.deleteCategory(99L));
    }

    /**
     * Tests that adding a category with a null name throws an IllegalArgumentException.
     * Ensures the service enforces non-null names for new categories.
     */
    @Test
    void addCategory_NullName_ThrowsIllegalArgumentException() {
        CategoryDto inputDto = new CategoryDto();
        inputDto.setId(1L);
        inputDto.setName(null);
        inputDto.setDescription("Description");

        assertThrows(IllegalArgumentException.class, () -> categoryServiceImpl.addCategory(inputDto));
    }

    /**
     * Tests that updating a category with an invalid ID throws a ResourceNotFoundException.
     * Ensures the service handles update requests for non-existent categories correctly.
     */
    @Test
    void updateCategory_InvalidId_ThrowsResourceNotFoundException() {
        CategoryDto updatedDto = new CategoryDto();
        updatedDto.setId(99L);
        updatedDto.setName("Updated Name");
        updatedDto.setDescription("Updated Description");

        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.updateCategory(updatedDto, 99L));
    }

    /**
     * Tests that retrieving all categories returns a list of CategoryDto objects.
     * Ensures the service correctly maps and returns multiple categories.
     */
    @Test
    void getAllCategories_ReturnsListOfCategoryDtos() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Category1");
        category1.setDescription("Description1");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Category2");
        category2.setDescription("Description2");

        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        List<CategoryDto> result = categoryServiceImpl.getAllCategories();

        assertEquals(2, result.size());
        assertEquals("Category1", result.get(0).getName());
        assertEquals("Category2", result.get(1).getName());
    }

    /**
     * Validates that addCategory returns the saved CategoryDto when given valid input.
     */
    @Test
    void addCategory_ReturnsSavedCategory_WhenValidInput() {
        // Arrange
        CategoryDto inputDto = new CategoryDto();
        inputDto.setName("Science");
        inputDto.setDescription("All about science");

        Category categoryEntity = new Category();
        categoryEntity.setName("Science");
        categoryEntity.setDescription("All about science");

        Category savedCategoryEntity = new Category();
        savedCategoryEntity.setId(2L);
        savedCategoryEntity.setName("Science");
        savedCategoryEntity.setDescription("All about science");

        CategoryDto savedDto = new CategoryDto();
        savedDto.setId(2L);
        savedDto.setName("Science");
        savedDto.setDescription("All about science");

        when(modelMapper.map(inputDto, Category.class)).thenReturn(categoryEntity);
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategoryEntity);
        when(modelMapper.map(savedCategoryEntity, CategoryDto.class)).thenReturn(savedDto);

        // Act
        CategoryDto result = categoryServiceImpl.addCategory(inputDto);

        // Assert
        assertEquals(savedDto.getId(), result.getId());
        assertEquals(savedDto.getName(), result.getName());
        assertEquals(savedDto.getDescription(), result.getDescription());
    }

    /**
     * Ensures that getCategory throws ResourceNotFoundException when the category does not exist.
     */
    @Test
    void getCategory_ThrowsException_WhenCategoryDoesNotExist() {
        // Arrange
        when(categoryRepository.findById(100L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.getCategory(100L));
    }

    /**
     * Validates that updateCategory updates and returns the category when given valid input.
     */
    @Test
    void updateCategory_UpdatesCategory_WhenValidInput() {
        // Arrange
        Category existingCategory = new Category();
        existingCategory.setId(1L);
        existingCategory.setName("Old Name");
        existingCategory.setDescription("Old Description");

        CategoryDto updatedDto = new CategoryDto();
        updatedDto.setName("New Name");
        updatedDto.setDescription("New Description");

        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName("New Name");
        updatedCategory.setDescription("New Description");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);
        when(modelMapper.map(updatedCategory, CategoryDto.class)).thenReturn(updatedDto);

        // Act
        CategoryDto result = categoryServiceImpl.updateCategory(updatedDto, 1L);

        // Assert
        assertEquals("New Name", result.getName());
        assertEquals("New Description", result.getDescription());
    }

    /**
     * Ensures that deleteCategory throws ResourceNotFoundException when the category does not exist.
     */
    @Test
    void deleteCategory_ThrowsException_WhenCategoryDoesNotExist() {
        // Arrange
        when(categoryRepository.findById(200L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> categoryServiceImpl.deleteCategory(200L));
    }

    /**
     * Validates that getAllCategories returns an empty list when no categories exist.
     */
    @Test
    void getAllCategories_ReturnsEmptyList_WhenNoCategoriesExist() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(List.of());

        // Act
        List<CategoryDto> result = categoryServiceImpl.getAllCategories();

        // Assert
        assertEquals(0, result.size());
    }
}