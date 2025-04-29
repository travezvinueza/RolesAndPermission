package com.develop.backend.domain.entity;

import com.develop.backend.application.dto.OrderDetailDto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;


    public static OrderDetail fromDto(OrderDetailDto orderDetailDto) {
        return OrderDetail.builder()
                .id(orderDetailDto.getId())
                .quantity(orderDetailDto.getQuantity())
                .unitPrice(orderDetailDto.getUnitPrice())
                .totalPrice(orderDetailDto.getTotalPrice())
                .order(Order.builder()
                        .id(orderDetailDto.getOrderId())
                        .build())
                .product(Product.builder()
                        .id(orderDetailDto.getProductId())
                        .build())
                .build();
    }
}
