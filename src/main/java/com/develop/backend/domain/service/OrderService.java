package com.develop.backend.domain.service;

import com.develop.backend.application.dto.OrderDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto findByIdOrder(Long id);
    OrderDto updateOrder(Long id, OrderDto orderDto);
    List<OrderDto> listOrders(Long userId);
    OrderDto processOrder(Long orderId);
    OrderDto sendOrder(Long orderId);
    OrderDto deliverOrder(Long orderId);
    OrderDto cancelOrder(Long orderId);
    OrderDto reactivateOrder(Long orderId);

    ResponseEntity<Resource> exportInvoice(Long idUser, List<Long> idOrder);
}
