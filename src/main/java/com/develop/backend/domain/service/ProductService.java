package com.develop.backend.domain.service;

import com.develop.backend.application.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);
    ProductDto updateProduct(Long id, ProductDto productDto, MultipartFile newImage) throws IOException;
    void deleteProduct(Long productId);
    Page<ProductDto> getAllProducts(String productName, Pageable pageable);
    ProductDto getProductById(Long productId);
}
