package com.develop.backend.domain.service.impl;

import com.develop.backend.application.dto.CategoryDto;
import com.develop.backend.domain.entity.Category;
import com.develop.backend.domain.repository.CategoryRepository;
import com.develop.backend.domain.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        if(categoryRepository.findByCategoryName(categoryDto.getCategoryName()).isPresent()) {
            throw new RuntimeException("Category already exists");
        }
        Category category = Category.fromDto(categoryDto);
        category = categoryRepository.save(category);
        return CategoryDto.fromEntity(category);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setCategoryName(categoryDto.getCategoryName());
        category = categoryRepository.save(category);
        return CategoryDto.fromEntity(category);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryDto::fromEntity)
                .toList();
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return CategoryDto.fromEntity(category);
    }
}
