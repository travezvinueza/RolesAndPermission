package com.develop.backend.domain.service.impl;

import com.develop.backend.application.dto.OrderDetailDto;
import com.develop.backend.domain.entity.OrderDetail;
import com.develop.backend.domain.repository.OrderDetailRepository;
import com.develop.backend.domain.repository.ProductRepository;
import com.develop.backend.domain.service.OrderDetailService;
import com.develop.backend.infrastructure.exception.ProductNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetailDto saveOrderDetail(OrderDetailDto orderDetailDto) {
        int updatedRows = productRepository.reduceInventory(orderDetailDto.getProductId(), orderDetailDto.getQuantity());
        if (updatedRows == 0) {
            throw new ProductNotFoundException("No hay suficiente stock para el producto ID: " + orderDetailDto.getProductId());
        }
        return OrderDetailDto.fromEntity(orderDetailRepository.save(OrderDetail.fromDto(orderDetailDto)));
    }

    @Override
    public OrderDetailDto getOrderDetailById(Long id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(() -> new RuntimeException("OrderDetail not found"));
        return OrderDetailDto.fromEntity(orderDetail);
    }

    @Override
    public OrderDetailDto updateOrderDetail(OrderDetailDto orderDetailDto) {
       OrderDetail orderDetail = orderDetailRepository.findById(orderDetailDto.getId()).orElseThrow(() -> new RuntimeException("OrderDetail not found for update"));

        int updatedRows = productRepository.reduceInventory(orderDetailDto.getProductId(), orderDetailDto.getQuantity());
        if (updatedRows == 0) {
            throw new ProductNotFoundException("No hay suficiente stock para el producto ID: " + orderDetailDto.getProductId());
        }
        orderDetail.setQuantity(orderDetailDto.getQuantity());
        return OrderDetailDto.fromEntity(orderDetailRepository.save(orderDetail));
    }

    @Override
    public List<OrderDetailDto> listOrderDetails() {
        return orderDetailRepository.findAll().stream().map(OrderDetailDto::fromEntity).toList();
    }

    @Override
    public void deleteOrderDetailById(Long id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(() -> new RuntimeException("OrderDetail not found for deletion"));
        orderDetailRepository.delete(orderDetail);
    }

}
