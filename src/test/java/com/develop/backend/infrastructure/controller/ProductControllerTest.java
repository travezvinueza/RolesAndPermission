package com.develop.backend.infrastructure.controller;

import com.develop.backend.application.dto.CategoryDto;
import com.develop.backend.application.dto.ProductDto;
import com.develop.backend.domain.service.FileUploadService;
import com.develop.backend.domain.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Mock
    private FileUploadService fileUploadService;

    private CategoryDto getDefaultCategoryDto() {
        return CategoryDto.builder().id(1L).categoryName("Electronics").build();
    }

    @Test
    void createProducts() throws IOException {
//        CategoryDto categoryDto = getDefaultCategoryDto();
//        ProductDto productDto1 = ProductDto.builder().productCode("P001").productName("Laptop").price(new BigDecimal("1200.00")).stock(10).categoryDto(categoryDto).build();
//        ProductDto productDto2 = ProductDto.builder().productCode("P002").productName("Mouse").price(new BigDecimal("25.00")).stock(50).categoryDto(categoryDto).build();
//        List<ProductDto> inputProducts = List.of(productDto1, productDto2);
//
//        ProductDto createdProductDto1 = ProductDto.builder().id(1L).productCode("P001").productName("Laptop").price(new BigDecimal("1200.00")).stock(10).categoryDto(categoryDto).build();
//        ProductDto createdProductDto2 = ProductDto.builder().id(2L).productCode("P002").productName("Mouse").price(new BigDecimal("25.00")).stock(50).categoryDto(categoryDto).build();
//        List<ProductDto> expectedProducts = List.of(createdProductDto1, createdProductDto2);
//
//        when(productService.createProduct(anyList())).thenReturn(expectedProducts);
//
//        ResponseEntity<List<ProductDto>> responseEntity = productController.createProducts(inputProducts, null);
//
//        assertNotNull(responseEntity);
//        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//        assertEquals(expectedProducts, responseEntity.getBody());
//        verify(productService).createProduct(eq(inputProducts));
    }

    @Test
    void updateProduct() throws IOException {
        Long productId = 2L;
        CategoryDto categoryDto = getDefaultCategoryDto();

        ProductDto inputDto = ProductDto.builder()
                .id(productId)
                .productCode("P002")
                .productName("Updated Mouse")
                .price(new BigDecimal("25.00"))
                .stock(100)
                .categoryDto(categoryDto)
                .build();

        ProductDto updatedDto = ProductDto.builder()
                .id(productId)
                .productCode("P002")
                .productName("Updated Mouse")
                .price(new BigDecimal("25.00"))
                .stock(100)
                .categoryDto(categoryDto)
                .build();

        when(productService.updateProduct(eq(productId), any(ProductDto.class), isNull()))
                .thenReturn(updatedDto);

        ResponseEntity<ProductDto> response = productController.updateProduct(inputDto, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Mouse", response.getBody().getProductName());
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
    void getProduct() {
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
    void getAllProducts() {
        List<ProductDto> products = List.of(
                ProductDto.builder()
                        .id(1L)
                        .productCode("P001")
                        .productName("Shampoo")
                        .price(new BigDecimal("10.00"))
                        .stock(50)
                        .imageProduct("image1.jpg")
                        .categoryDto(getDefaultCategoryDto())
                        .build(),
                ProductDto.builder()
                        .id(2L)
                        .productCode("P002")
                        .productName("Laptop")
                        .price(new BigDecimal("1200.00"))
                        .stock(10)
                        .imageProduct("image2.jpg")
                        .categoryDto(getDefaultCategoryDto())
                        .build()
        );

        Page<ProductDto> productPage = new PageImpl<>(products, PageRequest.of(0, 10), products.size());

        when(productService.getAllProducts(eq(null), any(Pageable.class))).thenReturn(productPage);

        ResponseEntity<Page<ProductDto>> responseEntity = productController.getAllProducts(null, 0, 10);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().getContent().size());
        assertEquals("Shampoo", responseEntity.getBody().getContent().get(0).getProductName());
        assertEquals("Laptop", responseEntity.getBody().getContent().get(1).getProductName());
        verify(productService).getAllProducts(eq(null), any(Pageable.class));
    }

}