package com.develop.backend.domain.service;

import com.develop.backend.application.dto.CategoryDto;
import org.springframework.data.domain.Page;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(Long id, CategoryDto categoryDto);

    void deleteCategory(Long id);

    Page<CategoryDto> getAllCategories(String categoryName, int page, int size);

    CategoryDto getCategoryById(Long id);
}
