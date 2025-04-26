package com.develop.backend.domain.entity;

import com.develop.backend.application.dto.CategoryDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_name", nullable = false, unique = true)
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<Product> products;


    public static Category fromDto(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .categoryName(categoryDto.getCategoryName())
                .build();
    }
}
