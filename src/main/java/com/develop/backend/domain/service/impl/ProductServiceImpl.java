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
import java.util.Optional;
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
    public ProductDto createProduct(ProductDto productDto) {
        if (productDto.getCategoryDto() == null ||
                productDto.getCategoryDto().getCategoryName() == null ||
                productDto.getCategoryDto().getCategoryName().trim().isEmpty()) {
            throw new CategoryNotFoundException("Category name must be provided");
        }

        Category category = categoryRepository.findByCategoryName(productDto.getCategoryDto().getCategoryName().trim())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        if (productRepository.findByProductName(productDto.getProductName()).isPresent()) {
            throw new ProductNotFoundException("Product with name '" + productDto.getProductName() + "' already exists");
        }

        Product product = Product.fromDto(productDto);
        product.setProductCode(generateProductCode());
        product.setCategory(category);
        product = productRepository.save(product);
        cacheService.invalidateCache("product:all");

        return ProductDto.fromEntity(product);
    }

    private String generateProductCode() {
        return "PRODUCT-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto, MultipartFile newImage) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found for update"));

        Optional<Product> existingProduct = productRepository.findByProductName(productDto.getProductName());
        if (existingProduct.isPresent() && !existingProduct.get().getId().equals(id)) {
            throw new ProductNotFoundException("Product with name '" + productDto.getProductName() + "' already exists");
        }

        if (productDto.getCategoryDto() == null || productDto.getCategoryDto().getId() == null) {
            throw new CategoryNotFoundException("Category ID must be provided");
        }

        Category category = categoryRepository.findById(productDto.getCategoryDto().getId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        product.setProductName(productDto.getProductName());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setCategory(category);

        if (newImage != null && !newImage.isEmpty()) {
            String fileUrl = fileUploadService.uploadFile(newImage, "products");
            if (product.getImageProduct() != null && !product.getImageProduct().isEmpty()) {
                fileUploadService.deleteUpload(product.getImageProduct());
            }
            product.setImageProduct(fileUrl);
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
