package com.develop.backend.domain.service.impl;

import com.develop.backend.application.dto.ProductDto;
import com.develop.backend.domain.entity.Category;
import com.develop.backend.domain.entity.Product;
import com.develop.backend.domain.repository.CategoryRepository;
import com.develop.backend.domain.repository.ProductRepository;
import com.develop.backend.domain.service.FileUploadService;
import com.develop.backend.domain.service.GenericCacheService;
import com.develop.backend.domain.service.ProductService;
import com.develop.backend.infrastructure.exception.CategoryNotFoundException;
import com.develop.backend.infrastructure.exception.ProductNotFoundException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FileUploadService fileUploadService;
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
    public ProductDto updateProduct(Long id, ProductDto productDto, MultipartFile newImage) throws IOException {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found for update"));
        product.setProductName(productDto.getProductName());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());

        if (newImage != null && !newImage.isEmpty()) {
            String fileUrl = fileUploadService.uploadFile(newImage, "products");
            if (product.getImageProduct() != null && !product.getImageProduct().isEmpty()) {
                fileUploadService.deleteUpload(product.getImageProduct());
            }
            product.setImageProduct(fileUrl);
        }

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
    public Page<ProductDto> getAllProducts(String productName, Pageable pageable) {
        Page<Product> productPage;

        if (productName != null && !productName.isBlank()) {
            productPage = productRepository.findByProductNameContaining(productName, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }
        return productPage.map(ProductDto::fromEntity);
    }


    @Override
    public ProductDto getProductById(Long productId) {
        String cacheKey = "product:" + productId;
        long start = System.currentTimeMillis();

        ProductDto result = cacheService.getFromCache(cacheKey, ProductDto.class)
                .orElseGet(() -> {
                    Product product = productRepository.findById(productId)
                            .orElseThrow(() -> new ProductNotFoundException("Product not found"));

                    ProductDto dto = ProductDto.fromEntity(product);
                    cacheService.saveToCache(cacheKey, dto);
                    return dto;
                });

        long duration = System.currentTimeMillis() - start;
        log.info("Tiempo total para getProductById({}): {} ms", productId, duration);

        return result;
    }

}
