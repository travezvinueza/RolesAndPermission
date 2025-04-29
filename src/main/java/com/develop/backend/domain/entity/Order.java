package com.develop.backend.domain.entity;

import com.develop.backend.application.dto.OrderDto;
import com.develop.backend.domain.enums.OrderState;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_code", nullable = false, unique = true)
    private String orderCode;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false)
    private Timestamp creationDate;

    @Enumerated(EnumType.STRING)
    private OrderState orderState;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    public static Order fromDto(OrderDto orderDto) {
        return Order.builder()
                .id(orderDto.getId())
                .orderCode(orderDto.getOrderCode())
                .orderState(orderDto.getOrderState())
                .description(orderDto.getDescription())
                .user(User.builder()
                        .id(orderDto.getUserId())
                        .build())
                .build();
    }

}
