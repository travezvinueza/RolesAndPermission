package com.develop.backend.domain.service.impl;

import com.develop.backend.application.dto.ProductDto;
import com.develop.backend.domain.entity.Product;
import com.develop.backend.domain.repository.ProductRepository;
import com.develop.backend.domain.service.ProductService;
import com.develop.backend.insfraestructure.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        if (productRepository.findByProductName(productDto.getProductName()).isPresent()) {
            throw new ProductNotFoundException("Product already exists");
        } else {
            productDto.setProductCode(generateProductCode());
            return ProductDto.fromEntity(productRepository.save(Product.fromDto(productDto)));
        }
    }

    private String generateProductCode() {
        Product lastProduct = productRepository.findTopByOrderByIdDesc();
        long productCount = (lastProduct != null) ? lastProduct.getId() + 1 : 1;
        return String.format("PRODUCT-%06d", productCount);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
     Product product = productRepository.findById(productDto.getId()).orElseThrow(() -> new ProductNotFoundException("Product not found for update"));
        product.setProductName(productDto.getProductName());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setImageProduct(productDto.getImageProduct());
        return ProductDto.fromEntity(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found for delete"));
        productRepository.delete(product);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream().map(ProductDto::fromEntity).toList();
    }

    @Override
    public ProductDto getProductById(Long productId) {
        return ProductDto.fromEntity(productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found")));
    }
}
