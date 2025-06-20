package com.develop.backend.infrastructure.controller;

import com.develop.backend.application.dto.ProductDto;
import com.develop.backend.domain.service.FileUploadService;
import com.develop.backend.domain.service.ProductService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Validated
@RestController
@RequestMapping("/v3/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final FileUploadService fileUploadService;


    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> createProducts(@RequestPart ProductDto productDto,
                                                     @RequestPart(required = false) MultipartFile imageProduct) throws IOException {
        if (imageProduct != null && !imageProduct.isEmpty()) {
            String imageUrl = fileUploadService.uploadFile(imageProduct, "products");
            productDto.setImageProduct(imageUrl);
        }

        return new ResponseEntity<>(productService.createProduct(productDto), HttpStatus.CREATED);
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDto> updateProduct(@RequestPart ProductDto productDto,
                                                    @RequestPart(required = false) MultipartFile newImage) throws IOException {
        return new ResponseEntity<>(productService.updateProduct(productDto.getId(), productDto, newImage), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<ProductDto>> getAllProducts(@RequestParam(required = false) String productName,
                                                           @RequestParam(defaultValue = "0") @Min(0) int page,
                                                           @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> products = productService.getAllProducts(productName, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }
}
