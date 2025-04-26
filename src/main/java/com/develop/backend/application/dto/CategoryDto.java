package com.develop.backend.application.dto;

import com.develop.backend.domain.entity.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    @NotBlank(message = "name category is required and cannot be blank.")
    private String categoryName;
    private List<ProductDto> products;

    
    public static CategoryDto fromEntity(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .products(category.getProducts().stream().map(ProductDto::fromEntity).toList())
                .build();
    }
}
