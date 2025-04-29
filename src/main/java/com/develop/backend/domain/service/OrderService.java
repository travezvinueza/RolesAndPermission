package com.develop.backend.domain.service;

import com.develop.backend.application.dto.OrderDto;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto processOrder(Long orderId);
    OrderDto sendOrder(Long orderId);
    OrderDto deliverOrder(Long orderId);
    OrderDto cancelOrder(Long orderId);
    OrderDto reactivateOrder(Long orderId);
    List<OrderDto> listOrders();
    OrderDto findById(Long id);
    ResponseEntity<Resource> exportInvoice(Long idUser, Long idOrder);
}
