package com.springapplication.blogappproject.service;

import com.springapplication.blogappproject.payload.CategoryDto;

public interface CategoryService {

    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto getCategory(Long categoryId);

}
