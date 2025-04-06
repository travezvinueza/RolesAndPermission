package com.develop.backend.domain.service;

import com.develop.backend.application.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    void deleteCategory(Long id);

    List<CategoryDto> getAllCategories();

    CategoryDto getCategoryById(Long id);
}
