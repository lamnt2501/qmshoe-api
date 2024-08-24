package com.lamdangfixbug.qmshoe.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamdangfixbug.qmshoe.order.payload.mapper.OrderMapper;
import com.lamdangfixbug.qmshoe.order.payload.request.OrderRequest;
import com.lamdangfixbug.qmshoe.order.payload.request.UpdateOrderStatusRequest;
import com.lamdangfixbug.qmshoe.order.payload.response.OrderResponse;
import com.lamdangfixbug.qmshoe.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/management/orders")
public class OrderManagementController {
    private final OrderService orderService;
    private final OrderMapper orderMapper;
    public OrderManagementController(OrderService orderService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderMapper = orderMapper;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrder(@RequestParam Map<String, Object> params) {
        return ResponseEntity.ok(orderService.getAllOrdersAdmin(params).stream().map(orderMapper::map).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderMapper.map(orderService.getOrderAdmin(id)));
    }

    @PatchMapping
    public ResponseEntity<OrderResponse> updateOrder(@RequestBody UpdateOrderStatusRequest request) {
        return new ResponseEntity<>(orderMapper.map(orderService.updateOrderAdmin(request)), HttpStatus.OK);
    }

    @GetMapping("/summary")
    public ResponseEntity<?> summary() {
        return ResponseEntity.ok(orderService.summary());
    }

    @GetMapping("/product-best-seller")
    public ResponseEntity<?> productBestSeller() {
        return ResponseEntity.ok(orderService.getProductBestSeller());
    }

    @GetMapping("/top-customer")
    public ResponseEntity<?> topCustomer() {
        return ResponseEntity.ok(orderService.getTopCustomer());
    }
}
