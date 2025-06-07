package com.develop.backend.infrastructure.controller;

import com.develop.backend.application.dto.CategoryDto;
import com.develop.backend.domain.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;

    @Mock
    private CategoryService categoryService;

    @Test
    void createCategory() {
        CategoryDto inputCategoryDto = CategoryDto.builder().categoryName("Electronics").build();
        CategoryDto expectedCategoryDto = CategoryDto.builder().id(1L).categoryName("Electronics").build();

        when(categoryService.createCategory(any(CategoryDto.class))).thenReturn(expectedCategoryDto);

        ResponseEntity<CategoryDto> responseEntity = categoryController.createCategory(inputCategoryDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedCategoryDto, responseEntity.getBody());
        verify(categoryService).createCategory(eq(inputCategoryDto));
    }

    @Test
    void updateCategory() {
        Long categoryId = 1L;
        CategoryDto inputCategoryDto = CategoryDto.builder().id(categoryId).categoryName("Home Appliances").build();
        CategoryDto expectedCategoryDto = CategoryDto.builder().id(categoryId).categoryName("Home Appliances").build();

        when(categoryService.updateCategory(anyLong(), any(CategoryDto.class))).thenReturn(expectedCategoryDto);

        ResponseEntity<CategoryDto> responseEntity = categoryController.updateCategory(inputCategoryDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedCategoryDto, responseEntity.getBody());
        verify(categoryService).updateCategory(eq(categoryId), eq(inputCategoryDto));
    }

    @Test
    void deleteCategory() {
        Long categoryIdToDelete = 1L;
        doNothing().when(categoryService).deleteCategory(anyLong());

        ResponseEntity<Void> responseEntity = categoryController.deleteCategory(categoryIdToDelete);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(categoryService).deleteCategory(eq(categoryIdToDelete));
    }

    @Test
    void getAllCategories() {
        CategoryDto category1 = CategoryDto.builder().id(1L).categoryName("Books").build();
        CategoryDto category2 = CategoryDto.builder().id(2L).categoryName("Movies").build();

        List<CategoryDto> categoriesList = List.of(category1, category2);
        Page<CategoryDto> categoryPage = new PageImpl<>(categoriesList);

        when(categoryService.getAllCategories(eq(null), any(Pageable.class))).thenReturn(categoryPage);

        ResponseEntity<Page<CategoryDto>> responseEntity = categoryController.getAllCategories(null, 0, 10);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().getContent().size());
        assertEquals("Books", responseEntity.getBody().getContent().get(0).getCategoryName());
        assertEquals("Movies", responseEntity.getBody().getContent().get(1).getCategoryName());

        verify(categoryService).getAllCategories(eq(null), any(Pageable.class));
    }


    @Test
    void getCategoryById() {
        Long categoryId = 1L;
        CategoryDto expectedCategory = CategoryDto.builder().id(categoryId).categoryName("Music").build();

        when(categoryService.getCategoryById(anyLong())).thenReturn(expectedCategory);

        ResponseEntity<CategoryDto> responseEntity = categoryController.getCategoryById(categoryId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedCategory, responseEntity.getBody());
        verify(categoryService).getCategoryById(eq(categoryId));
    }
}