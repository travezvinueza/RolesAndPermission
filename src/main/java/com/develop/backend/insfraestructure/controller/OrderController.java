package com.develop.backend.insfraestructure.controller;

import com.develop.backend.application.dto.OrderDto;
import com.develop.backend.domain.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/update")
    public ResponseEntity<OrderDto> updateOrder(@RequestBody OrderDto orderDto) {
        return new ResponseEntity<>(orderService.updateOrder(orderDto), HttpStatus.OK);
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<OrderDto>> listOrders(@PathVariable Long userId) {
        return new ResponseEntity<>(orderService.listOrders(userId), HttpStatus.OK);
    }

    @PutMapping("/process/{orderId}")
    public ResponseEntity<OrderDto> processOrder(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.processOrder(orderId), HttpStatus.OK);
    }

    @PutMapping("/send/{orderId}")
    public ResponseEntity<OrderDto> sendOrder(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.sendOrder(orderId), HttpStatus.OK);
    }

    @PutMapping("/deliver/{orderId}")
    public ResponseEntity<OrderDto> deliverOrder(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.deliverOrder(orderId), HttpStatus.OK);
    }

    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.cancelOrder(orderId), HttpStatus.OK);
    }

    @PutMapping("/reactivate/{orderId}")
    public ResponseEntity<OrderDto> reactivateOrder(@PathVariable Long orderId) {
        return new ResponseEntity<>(orderService.reactivateOrder(orderId), HttpStatus.OK);
    }

//    @PostMapping("/export")
//    public ResponseEntity<Resource> exportInvoice(@RequestBody Long idOrder) {
//        return orderService.exportInvoice(idOrder);
//    }

    @GetMapping("/findByIdOrder/{id}")
    public ResponseEntity<OrderDto> findByIdOrder(@PathVariable Long id) {
        return new ResponseEntity<>(orderService.findByIdOrder(id), HttpStatus.OK);
    }
}
