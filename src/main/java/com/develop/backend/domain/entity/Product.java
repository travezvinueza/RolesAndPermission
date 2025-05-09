package com.develop.backend.domain.entity;

import com.develop.backend.application.dto.ProductDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_code", nullable = false, unique = true)
    private String productCode;

    @Column(name = "product_name", nullable = false, unique = true)
    private String productName;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "image_product")
    private String imageProduct;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    public static Product fromDto(ProductDto productDto) {
        return Product.builder()
                .id(productDto.getId())
                .productCode(productDto.getProductCode())
                .productName(productDto.getProductName())
                .price(productDto.getPrice())
                .stock(productDto.getStock())
                .imageProduct(productDto.getImageProduct())
                .category(productDto.getCategoryDto() != null ? Category.fromDto(productDto.getCategoryDto()) : null)
                .build();
    }
}
