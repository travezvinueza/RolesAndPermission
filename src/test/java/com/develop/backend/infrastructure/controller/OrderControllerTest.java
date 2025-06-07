package com.develop.backend.infrastructure.controller;

import com.develop.backend.application.dto.OrderDetailDto;
import com.develop.backend.application.dto.OrderDto;
import com.develop.backend.domain.enums.OrderState;
import com.develop.backend.domain.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    private OrderDetailDto getDefaultOrderDetailDto() {
        return OrderDetailDto.builder()
                .id(1L)
                .productId(101L)
                .quantity(2)
                .unitPrice(new BigDecimal("50.00"))
                .totalPrice(new BigDecimal("100.00"))
                .build();
    }

    private OrderDto getDefaultOrderDto() {
        return OrderDto.builder()
                .id(1L)
                .orderCode("ORD123")
                .creationDate(Timestamp.from(Instant.now()))
                .orderState(OrderState.PENDING)
                .description("Test Order")
                .userId(1L)
                .orderDetails(List.of(getDefaultOrderDetailDto()))
                .build();
    }

    @Test
    void createOrder() {
        OrderDto inputOrderDto = getDefaultOrderDto();
        inputOrderDto.setId(null);
        OrderDto expectedOrderDto = getDefaultOrderDto();
        expectedOrderDto.setId(1L);

        when(orderService.createOrder(any(OrderDto.class))).thenReturn(expectedOrderDto);

        ResponseEntity<OrderDto> responseEntity = orderController.createOrder(inputOrderDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedOrderDto, responseEntity.getBody());
        verify(orderService).createOrder(eq(inputOrderDto));
    }

    @Test
    void updateOrder() {
        OrderDto inputOrderDto = getDefaultOrderDto();
        inputOrderDto.setDescription("Updated Test Order");
        OrderDto expectedOrderDto = getDefaultOrderDto();
        expectedOrderDto.setDescription("Updated Test Order");


        when(orderService.updateOrder(anyLong(), any(OrderDto.class))).thenReturn(expectedOrderDto);

        ResponseEntity<OrderDto> responseEntity = orderController.updateOrder(inputOrderDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedOrderDto, responseEntity.getBody());
        verify(orderService).updateOrder(eq(inputOrderDto.getId()), eq(inputOrderDto));
    }

    @Test
    void listOrders() {
        Long userId = 1L;
        OrderDto order1 = getDefaultOrderDto();
        OrderDto order2 = getDefaultOrderDto();
        order2.setId(2L);
        order2.setOrderCode("ORD124");
        List<OrderDto> expectedOrders = List.of(order1, order2);

        when(orderService.listOrders(anyLong())).thenReturn(expectedOrders);

        ResponseEntity<List<OrderDto>> responseEntity = orderController.listOrders(userId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedOrders, responseEntity.getBody());
        verify(orderService).listOrders(eq(userId));
    }

    @Test
    void processOrder() {
        Long orderId = 1L;
        OrderDto expectedOrderDto = getDefaultOrderDto();

        when(orderService.processOrder(anyLong())).thenReturn(expectedOrderDto);

        ResponseEntity<OrderDto> responseEntity = orderController.processOrder(orderId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedOrderDto, responseEntity.getBody());
        verify(orderService).processOrder(eq(orderId));
    }

    @Test
    void sendOrder() {
        Long orderId = 1L;
        OrderDto expectedOrderDto = getDefaultOrderDto();

        when(orderService.sendOrder(anyLong())).thenReturn(expectedOrderDto);

        ResponseEntity<OrderDto> responseEntity = orderController.sendOrder(orderId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedOrderDto, responseEntity.getBody());
        verify(orderService).sendOrder(eq(orderId));
    }

    @Test
    void deliverOrder() {
        Long orderId = 1L;
        OrderDto expectedOrderDto = getDefaultOrderDto();

        when(orderService.deliverOrder(anyLong())).thenReturn(expectedOrderDto);

        ResponseEntity<OrderDto> responseEntity = orderController.deliverOrder(orderId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedOrderDto, responseEntity.getBody());
        verify(orderService).deliverOrder(eq(orderId));
    }

    @Test
    void cancelOrder() {
        Long orderId = 1L;
        OrderDto expectedOrderDto = getDefaultOrderDto();

        when(orderService.cancelOrder(anyLong())).thenReturn(expectedOrderDto);

        ResponseEntity<OrderDto> responseEntity = orderController.cancelOrder(orderId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedOrderDto, responseEntity.getBody());
        verify(orderService).cancelOrder(eq(orderId));
    }

    @Test
    void reactivateOrder() {
        Long orderId = 1L;
        OrderDto expectedOrderDto = getDefaultOrderDto();

        when(orderService.reactivateOrder(anyLong())).thenReturn(expectedOrderDto);

        ResponseEntity<OrderDto> responseEntity = orderController.reactivateOrder(orderId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedOrderDto, responseEntity.getBody());
        verify(orderService).reactivateOrder(eq(orderId));
    }

    @Test
    void findByIdOrder_whenOrderFound() {
        Long orderId = 1L;
        OrderDto expectedOrderDto = getDefaultOrderDto();

        when(orderService.findByIdOrder(anyLong())).thenReturn(expectedOrderDto);

        ResponseEntity<OrderDto> responseEntity = orderController.findByIdOrder(orderId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedOrderDto, responseEntity.getBody());
        verify(orderService).findByIdOrder(eq(orderId));
    }
}