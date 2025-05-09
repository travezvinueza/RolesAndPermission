package com.develop.backend.domain.service;

import com.develop.backend.application.dto.OrderDetailDto;

import java.util.List;

public interface OrderDetailService {
    OrderDetailDto saveOrderDetail(OrderDetailDto orderDetailDto);
    OrderDetailDto getOrderDetailById(Long id);
    OrderDetailDto updateOrderDetail(OrderDetailDto orderDetailDto);
    List<OrderDetailDto> listOrderDetails();
    void deleteOrderDetailById(Long id);
}
