package com.develop.backend.domain.service;

import com.develop.backend.application.dto.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(Long id, CategoryDto categoryDto);

    void deleteCategory(Long id);

    Page<CategoryDto> getAllCategories(String categoryName, Pageable pageable);

    CategoryDto getCategoryById(Long id);
}
