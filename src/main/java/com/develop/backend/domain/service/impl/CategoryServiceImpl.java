package com.develop.backend.domain.service.impl;

import com.develop.backend.application.dto.CategoryDto;
import com.develop.backend.domain.entity.Category;
import com.develop.backend.domain.repository.CategoryRepository;
import com.develop.backend.domain.service.CategoryService;
import com.develop.backend.infrastructure.exception.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if(categoryRepository.findByCategoryName(categoryDto.getCategoryName()).isPresent()) {
            throw new CategoryNotFoundException("Category already exists");
        }
        Category category = Category.fromDto(categoryDto);
        category = categoryRepository.save(category);
        return CategoryDto.fromEntity(category);
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found for update"));
        category.setCategoryName(categoryDto.getCategoryName());
        category = categoryRepository.save(category);
        return CategoryDto.fromEntity(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found for deletion"));
        categoryRepository.delete(category);
    }

    @Override
    public Page<CategoryDto> getAllCategories(String categoryName, Pageable pageable) {
       Page<Category> categoryPage;

       if (categoryName != null && !categoryName.isBlank()) {
           categoryPage = categoryRepository.findByCategoryNameContaining(categoryName, pageable);
       } else {
           categoryPage = categoryRepository.findAll(pageable);
       }
       return categoryPage.map(CategoryDto::fromEntity);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        return CategoryDto.fromEntity(category);
    }
}
