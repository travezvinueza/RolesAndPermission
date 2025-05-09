package com.develop.backend.insfraestructure.controller;

import com.develop.backend.application.dto.OrderDto;
import com.develop.backend.domain.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v3/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) {
        return new ResponseEntity<>(orderService.createOrder(orderDto), HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<OrderDto> updateOrder(@RequestBody OrderDto orderDto) {
        return new ResponseEntity<>(orderService.updateOrder(orderDto), HttpStatus.OK);
    }

    @PostMapping("/list")
    public ResponseEntity<List<OrderDto>> listOrders(@RequestBody Long userId) {
        return new ResponseEntity<>(orderService.listOrders(userId), HttpStatus.OK);
    }

    @PostMapping("/process")
    public ResponseEntity<OrderDto> processOrder(@RequestBody Long orderId) {
        return new ResponseEntity<>(orderService.processOrder(orderId), HttpStatus.OK);
    }

    @PostMapping("/send")
    public ResponseEntity<OrderDto> sendOrder(@RequestBody Long orderId) {
        return new ResponseEntity<>(orderService.sendOrder(orderId), HttpStatus.OK);
    }

    @PostMapping("/deliver")
    public ResponseEntity<OrderDto> deliverOrder(@RequestBody Long orderId) {
        return new ResponseEntity<>(orderService.deliverOrder(orderId), HttpStatus.OK);
    }

    @PostMapping("/cancel")
    public ResponseEntity<OrderDto> cancelOrder(@RequestBody Long orderId) {
        return new ResponseEntity<>(orderService.cancelOrder(orderId), HttpStatus.OK);
    }

    @PostMapping("/reactivate")
    public ResponseEntity<OrderDto> reactivateOrder(@RequestBody Long orderId) {
        return new ResponseEntity<>(orderService.reactivateOrder(orderId), HttpStatus.OK);
    }

//    @PostMapping("/export")
//    public ResponseEntity<Resource> exportInvoice(@RequestBody Long idOrder) {
//        return orderService.exportInvoice(idOrder);
//    }
    @PostMapping("/find")
    public ResponseEntity<OrderDto> findByIdOrder(@RequestBody Long id) {
        return new ResponseEntity<>(orderService.findByIdOrder(id), HttpStatus.OK);
    }
}
