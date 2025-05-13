package com.develop.backend.application.dto;

import com.develop.backend.domain.entity.OrderDetail;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {
    private Long id;
    private Long productId;
//    private Long orderId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;


    public static OrderDetailDto fromEntity(OrderDetail orderDetail) {
        return OrderDetailDto.builder()
                .id(orderDetail.getId())
                .productId(orderDetail.getProduct().getId())
//                .orderId(orderDetail.getOrder().getId())
                .quantity(orderDetail.getQuantity())
                .unitPrice(orderDetail.getUnitPrice())
                .totalPrice(orderDetail.getTotalPrice())
                .build();
    }
}
