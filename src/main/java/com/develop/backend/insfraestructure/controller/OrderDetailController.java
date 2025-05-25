package com.develop.backend.insfraestructure.controller;

import com.develop.backend.application.dto.OrderDetailDto;
import com.develop.backend.domain.service.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v3/order-detail")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @PostMapping("/create")
    public ResponseEntity<OrderDetailDto> createOrderDetail(@RequestBody OrderDetailDto orderDetailDto) {
        OrderDetailDto savedOrderDetail = orderDetailService.saveOrderDetail(orderDetailDto);
        return new ResponseEntity<>(savedOrderDetail, HttpStatus.CREATED);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<OrderDetailDto> getOrderDetailById(@PathVariable Long id) {
        OrderDetailDto orderDetailDto = orderDetailService.getOrderDetailById(id);
        return ResponseEntity.ok(orderDetailDto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<OrderDetailDto> updateOrderDetail(@PathVariable Long id, @RequestBody OrderDetailDto orderDetailDto) {
        orderDetailDto.setId(id);
        OrderDetailDto updatedOrderDetail = orderDetailService.updateOrderDetail(orderDetailDto);
        return ResponseEntity.ok(updatedOrderDetail);
    }

    @GetMapping("/list")
    public ResponseEntity<List<OrderDetailDto>> listOrderDetails() {
        List<OrderDetailDto> orderDetails = orderDetailService.listOrderDetails();
        return ResponseEntity.ok(orderDetails);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrderDetailById(@PathVariable Long id) {
        orderDetailService.deleteOrderDetailById(id);
        return ResponseEntity.noContent().build();
    }

}
