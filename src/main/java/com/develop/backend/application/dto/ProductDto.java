package com.develop.backend.application.dto;

import com.develop.backend.domain.entity.Product;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String productCode;
    private String productName;
    private BigDecimal price;
    private int stock;
    private String imageProduct;
    private CategoryDto categoryDto;


    public static ProductDto fromEntity(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .productCode(product.getProductCode())
                .productName(product.getProductName())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageProduct(product.getImageProduct())
                .categoryDto(CategoryDto.fromEntity(product.getCategory()))
                .build();
    }
}
