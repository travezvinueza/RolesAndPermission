package com.develop.backend.domain.service;

import com.develop.backend.application.dto.ProductDto;

import java.util.List;

public interface ProductService {
    List<ProductDto> createProduct(List<ProductDto> productDto);
    ProductDto updateProduct(Long id, ProductDto productDto);
    void deleteProduct(Long productId);
    List<ProductDto> getAllProducts();
    ProductDto getProductById(Long productId);
}
