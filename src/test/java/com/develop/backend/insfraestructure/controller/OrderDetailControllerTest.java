package com.develop.backend.insfraestructure.controller;

import com.develop.backend.application.dto.OrderDetailDto;
import com.develop.backend.domain.service.OrderDetailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderDetailControllerTest {

    @InjectMocks
    private OrderDetailController orderDetailController;

    @Mock
    private OrderDetailService orderDetailService;

    private OrderDetailDto getDefaultOrderDetailDto(Long id) {
        return OrderDetailDto.builder()
                .id(id)
                .productId(101L)
                .quantity(2)
                .unitPrice(new BigDecimal("25.50"))
                .totalPrice(new BigDecimal("51.00"))
                .build();
    }

    @Test
    void createOrderDetail() {
        OrderDetailDto inputDto = getDefaultOrderDetailDto(null); // ID is null for creation
        OrderDetailDto expectedDto = getDefaultOrderDetailDto(1L); // ID is set after creation

        when(orderDetailService.saveOrderDetail(any(OrderDetailDto.class))).thenReturn(expectedDto);

        ResponseEntity<OrderDetailDto> responseEntity = orderDetailController.createOrderDetail(inputDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(expectedDto, responseEntity.getBody());
        verify(orderDetailService).saveOrderDetail(eq(inputDto));
    }

    @Test
    void getOrderDetailById() {
        Long detailId = 1L;
        OrderDetailDto expectedDto = getDefaultOrderDetailDto(detailId);

        when(orderDetailService.getOrderDetailById(anyLong())).thenReturn(expectedDto);

        ResponseEntity<OrderDetailDto> responseEntity = orderDetailController.getOrderDetailById(detailId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedDto, responseEntity.getBody());
        verify(orderDetailService).getOrderDetailById(eq(detailId));
    }

    @Test
    void updateOrderDetail() {
        Long detailId = 1L;
        OrderDetailDto inputDto = OrderDetailDto.builder()
                                    .productId(102L)
                                    .quantity(3)
                                    .unitPrice(new BigDecimal("30.00"))
                                    .totalPrice(new BigDecimal("90.00"))
                                    .build(); // ID not set here, will be set from path variable
        OrderDetailDto dtoWithId = OrderDetailDto.builder()
                                        .id(detailId) // ID set
                                        .productId(102L)
                                        .quantity(3)
                                        .unitPrice(new BigDecimal("30.00"))
                                        .totalPrice(new BigDecimal("90.00"))
                                        .build();
        OrderDetailDto expectedDto = getDefaultOrderDetailDto(detailId); // Service returns this
        expectedDto.setQuantity(3); // reflect update
        expectedDto.setTotalPrice(new BigDecimal("76.50")); // 25.50 * 3, assuming unit price from getDefault doesn't change or service handles it

        when(orderDetailService.updateOrderDetail(any(OrderDetailDto.class))).thenReturn(expectedDto);

        ResponseEntity<OrderDetailDto> responseEntity = orderDetailController.updateOrderDetail(detailId, inputDto);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedDto, responseEntity.getBody());
        // Verify that the DTO passed to the service has the ID set from the path variable
        verify(orderDetailService).updateOrderDetail(eq(dtoWithId));
    }

    @Test
    void listOrderDetails_whenDetailsExist() {
        OrderDetailDto detail1 = getDefaultOrderDetailDto(1L);
        OrderDetailDto detail2 = getDefaultOrderDetailDto(2L);
        detail2.setProductId(102L);
        List<OrderDetailDto> expectedDetails = List.of(detail1, detail2);

        when(orderDetailService.listOrderDetails()).thenReturn(expectedDetails);

        ResponseEntity<List<OrderDetailDto>> responseEntity = orderDetailController.listOrderDetails();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedDetails, responseEntity.getBody());
        verify(orderDetailService).listOrderDetails();
    }

    @Test
    void listOrderDetails_whenNoDetailsExist() {
        when(orderDetailService.listOrderDetails()).thenReturn(Collections.emptyList());

        ResponseEntity<List<OrderDetailDto>> responseEntity = orderDetailController.listOrderDetails();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Collections.emptyList(), responseEntity.getBody());
        verify(orderDetailService).listOrderDetails();
    }

    @Test
    void deleteOrderDetailById() {
        Long detailIdToDelete = 1L;
        doNothing().when(orderDetailService).deleteOrderDetailById(anyLong());

        ResponseEntity<Void> responseEntity = orderDetailController.deleteOrderDetailById(detailIdToDelete);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(orderDetailService).deleteOrderDetailById(eq(detailIdToDelete));
    }
}
