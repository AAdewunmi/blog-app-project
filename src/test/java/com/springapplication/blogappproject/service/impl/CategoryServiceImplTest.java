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

@SpringBootTest
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    /**
     * Tests the `addCategory` method in `CategoryServiceImpl`, which is responsible
     * for mapping a `CategoryDto` to a `Category` entity, saving it to the database,
     * and returning the saved entity mapped back to a `CategoryDto`.
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