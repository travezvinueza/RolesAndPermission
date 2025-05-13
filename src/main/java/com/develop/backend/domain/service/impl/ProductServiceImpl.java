package com.develop.backend.domain.service.impl;

import com.develop.backend.application.dto.ProductDto;
import com.develop.backend.domain.entity.Category;
import com.develop.backend.domain.entity.Product;
import com.develop.backend.domain.repository.CategoryRepository;
import com.develop.backend.domain.repository.ProductRepository;
import com.develop.backend.domain.service.GenericCacheService;
import com.develop.backend.domain.service.ProductService;
import com.develop.backend.insfraestructure.exception.CategoryNotFoundException;
import com.develop.backend.insfraestructure.exception.ProductNotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final GenericCacheService cacheService;


    @Override
    public List<ProductDto> createProduct(List<ProductDto> productDto) {
        List<Product> productsToSave = new ArrayList<>();
        for (ProductDto dto : productDto) {
            if (productRepository.findByProductName(dto.getProductName()).isPresent()) {
                throw new ProductNotFoundException("Product with name '" + dto.getProductName() + "' already exists");
            }
            dto.setProductCode(generateProductCode());

            Category category = null;
            if (dto.getCategoryDto().getId() != null) {
                category = categoryRepository.findById(dto.getCategoryDto().getId())
                        .orElseThrow(() -> new CategoryNotFoundException("Category with id '" + dto.getCategoryDto().getId() + "' not encountered"));
            } else if (dto.getCategoryDto().getCategoryName() != null) {
                category = categoryRepository.findByCategoryName(dto.getCategoryDto().getCategoryName())
                        .orElseThrow(() -> new CategoryNotFoundException("Category '" + dto.getCategoryDto().getCategoryName() + "' not foundtened"));
            } else {
                throw new CategoryNotFoundException("Category id or category name must be provided.");
            }

            Product product = Product.fromDto(dto);
            product.setCategory(category);
            productsToSave.add(product);
        }

        List<Product> savedProducts = productRepository.saveAll(productsToSave);
        cacheService.invalidateCache("product:all");
        return savedProducts.stream().map(ProductDto::fromEntity).toList();
    }

    private String generateProductCode() {
        return "PRODUCT-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId()).orElseThrow(() -> new ProductNotFoundException("Product not found for update"));
        product.setProductName(productDto.getProductName());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setImageProduct(productDto.getImageProduct());

        if (productDto.getCategoryDto() != null) {
            Category category = null;
            if (productDto.getCategoryDto().getId() != null) {
                category = categoryRepository.findById(productDto.getCategoryDto().getId())
                        .orElseThrow(() -> new CategoryNotFoundException("Category with id '" + productDto.getCategoryDto().getId() + "' not found"));
            } else if (productDto.getCategoryDto().getCategoryName() != null) {
                category = categoryRepository.findByCategoryName(productDto.getCategoryDto().getCategoryName())
                        .orElseThrow(() -> new CategoryNotFoundException("Category '" + productDto.getCategoryDto().getCategoryName() + "' not found"));
            } else {
                throw new CategoryNotFoundException("Category id or category name must be provided.");
            }
            product.setCategory(category);
        }

        Product updatedProduct = productRepository.save(product);
        // Actualizar cache específica
        String productKey = "product:" + updatedProduct.getId();
        cacheService.saveToCache(productKey, ProductDto.fromEntity(updatedProduct));

        // Invalidar cache general
        cacheService.invalidateCache("product:all");
        return ProductDto.fromEntity(updatedProduct);
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.findById(productId).ifPresent(product -> {
            productRepository.delete(product);
            cacheService.invalidateCache("product:" + productId);
            cacheService.invalidateCache("product:all");
        });
    }

    @Override
    public List<ProductDto> getAllProducts() {
        String cacheKey = "product:all";

        List<ProductDto> cachedList = cacheService.getListFromCache(cacheKey, new TypeReference<>() {
        });
        if (cachedList != null) return cachedList;

        List<ProductDto> dtoList = productRepository.findAll()
                .stream()
                .map(ProductDto::fromEntity)
                .toList();

        cacheService.saveToCache(cacheKey, dtoList);
        return dtoList;
    }

    @Override
    public ProductDto getProductById(Long productId) {
        String cacheKey = "product:" + productId;

        return cacheService.getFromCache(cacheKey, ProductDto.class)
                .orElseGet(() -> {
                    Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new ProductNotFoundException("Product not found"));

                    ProductDto dto = ProductDto.fromEntity(product);
                    cacheService.saveToCache(cacheKey, dto);
                    return dto;
                });

    }

}
