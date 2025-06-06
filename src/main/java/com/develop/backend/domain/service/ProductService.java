package com.develop.backend.domain.service;

import com.develop.backend.application.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    List<ProductDto> createProduct(List<ProductDto> productDto);
    ProductDto updateProduct(Long id, ProductDto productDto, MultipartFile newImage) throws IOException;
    void deleteProduct(Long productId);
    Page<ProductDto> getAllProducts(String productName, int page, int size);
    ProductDto getProductById(Long productId);
}
