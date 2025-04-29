package com.develop.backend.application.dto;

import com.develop.backend.domain.entity.Order;
import com.develop.backend.domain.enums.OrderState;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private String orderCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS", timezone = "America/Guayaquil")
    private Timestamp creationDate;
    private OrderState orderState;
    private String description;
    private Long userId;
    private List<OrderDetailDto> orderDetails;


    public static OrderDto fromEntity(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .creationDate(order.getCreationDate())
                .orderState(order.getOrderState())
                .description(order.getDescription())
                .userId(order.getUser().getId())
                .build();
    }
}
