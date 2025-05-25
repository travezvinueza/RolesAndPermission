package com.develop.backend.insfraestructure.controller;

import com.develop.backend.application.dto.CategoryDto;
import com.develop.backend.application.dto.ProductDto;
import com.develop.backend.domain.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private CategoryDto getDefaultCategoryDto() {
        return CategoryDto.builder().id(1L).categoryName("Electronics").build();
    }

    @Test
    void createProducts() {
        CategoryDto categoryDto = getDefaultCategoryDto();
        ProductDto productDto1 = ProductDto.builder().productCode("P001").productName("Laptop").price(new BigDecimal("1200.00")).stock(10).categoryDto(categoryDto).build();
        ProductDto productDto2 = ProductDto.builder().productCode("P002").productName("Mouse").price(new BigDecimal("25.00")).stock(50).categoryDto(categoryDto).build();
        List<ProductDto> inputProducts = List.of(productDto1, productDto2);

        ProductDto createdProductDto1 = ProductDto.builder().id(1L).productCode("P001").productName("Laptop").price(new BigDecimal("1200.00")).stock(10).categoryDto(categoryDto).build();
        ProductDto createdProductDto2 = ProductDto.builder().id(2L).productCode("P002").productName("Mouse").price(new BigDecimal("25.00")).stock(50).categoryDto(categoryDto).build();
        List<ProductDto> expectedProducts = List.of(createdProductDto1, createdProductDto2);

        when(productService.createProduct(anyList())).thenReturn(expectedProducts);

        ResponseEntity<List<ProductDto>> responseEntity = productController.createProducts(inputProducts);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedProducts, responseEntity.getBody());
        verify(productService).createProduct(eq(inputProducts));
    }

    @Test
    void updateProduct() {
        Long productId = 1L;
        CategoryDto categoryDto = getDefaultCategoryDto();
        ProductDto productToUpdate = ProductDto.builder().id(productId).productCode("P001_updated").productName("Laptop Updated").price(new BigDecimal("1250.00")).stock(5).categoryDto(categoryDto).build();
        ProductDto expectedProduct = ProductDto.builder().id(productId).productCode("P001_updated").productName("Laptop Updated").price(new BigDecimal("1250.00")).stock(5).categoryDto(categoryDto).build();

        when(productService.updateProduct(anyLong(), any(ProductDto.class))).thenReturn(expectedProduct);

        ResponseEntity<ProductDto> responseEntity = productController.updateProduct(productToUpdate);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedProduct, responseEntity.getBody());
        verify(productService).updateProduct(eq(productId), eq(productToUpdate));
    }

    @Test
    void deleteProduct() {
        Long productIdToDelete = 1L;
        doNothing().when(productService).deleteProduct(anyLong());

        ResponseEntity<Void> responseEntity = productController.deleteProduct(productIdToDelete);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(productService).deleteProduct(eq(productIdToDelete));
    }

    @Test
    void getProduct_whenProductFound() {
        Long productId = 1L;
        CategoryDto categoryDto = getDefaultCategoryDto();
        ProductDto expectedProduct = ProductDto.builder().id(productId).productCode("P003").productName("Keyboard").price(new BigDecimal("75.00")).stock(30).categoryDto(categoryDto).build();

        when(productService.getProductById(anyLong())).thenReturn(expectedProduct);

        ResponseEntity<ProductDto> responseEntity = productController.getProduct(productId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedProduct, responseEntity.getBody());
        verify(productService).getProductById(eq(productId));
    }

    @Test
    void getAllProducts_whenProductsExist() {
        CategoryDto categoryDto = getDefaultCategoryDto();
        ProductDto product1 = ProductDto.builder().id(1L).productCode("P001").productName("Laptop").price(new BigDecimal("1200.00")).stock(10).categoryDto(categoryDto).build();
        ProductDto product2 = ProductDto.builder().id(2L).productCode("P002").productName("Mouse").price(new BigDecimal("25.00")).stock(50).categoryDto(categoryDto).build();
        List<ProductDto> expectedProducts = List.of(product1, product2);

        when(productService.getAllProducts()).thenReturn(expectedProducts);

        ResponseEntity<List<ProductDto>> responseEntity = productController.getAllProducts();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedProducts, responseEntity.getBody());
        verify(productService).getAllProducts();
    }

    @Test
    void getAllProducts_whenNoProductsExist() {
        when(productService.getAllProducts()).thenReturn(Collections.emptyList());

        ResponseEntity<List<ProductDto>> responseEntity = productController.getAllProducts();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Collections.emptyList(), responseEntity.getBody());
        verify(productService).getAllProducts();
    }
}
