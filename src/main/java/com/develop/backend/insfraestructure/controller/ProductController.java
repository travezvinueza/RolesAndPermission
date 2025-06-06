package com.develop.backend.insfraestructure.controller;

import com.develop.backend.application.dto.ProductDto;
import com.develop.backend.domain.service.FileUploadService;
import com.develop.backend.domain.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v3/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final FileUploadService fileUploadService;


    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ProductDto>> createProducts(@RequestPart List<ProductDto> productDto,
                                                           @RequestPart(required = false) MultipartFile imageProduct) throws IOException {
        if (imageProduct != null && !imageProduct.isEmpty()) {
            String fileUrl = fileUploadService.uploadFile(imageProduct, "products");
            productDto.forEach(product -> product.setImageProduct(fileUrl));
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
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "5") int size) {
        Page<ProductDto> products = productService.getAllProducts(productName, page, size);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }
}
