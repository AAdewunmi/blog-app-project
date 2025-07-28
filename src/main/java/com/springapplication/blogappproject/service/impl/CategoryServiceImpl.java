package com.springapplication.blogappproject.service.impl;

import com.springapplication.blogappproject.entity.Category;
import com.springapplication.blogappproject.payload.CategoryDto;
import com.springapplication.blogappproject.repository.CategoryRepository;
import com.springapplication.blogappproject.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDto.class);
    }

    @Override
    public CategoryDto getCategory(Long categoryId) {
        return null;
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return List.of();
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long categoryId) {
        return null;
    }

    @Override
    public void deleteCategory(Long categoryId) {

    }
}
