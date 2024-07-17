package com.lamdangfixbug.qmshoe.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamdangfixbug.qmshoe.order.payload.request.OrderItemRequest;
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
@RequestMapping("api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrder(@RequestParam Map<String,Object> params) {
        return ResponseEntity.ok(orderService.getAllOrders(params).stream().map(OrderResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(OrderResponse.from(orderService.getOrder(id)));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody String order) throws IOException {
        OrderRequest orderRequest = new OrderRequest();
        ObjectMapper mapper = new ObjectMapper();
        orderRequest.setItems(mapper.readValue(order, OrderItemRequest[].class));
        return new ResponseEntity<>(OrderResponse.from(orderService.placeOrder(orderRequest)), HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<OrderResponse> updateOrder(@RequestBody UpdateOrderStatusRequest request) {
        return new ResponseEntity<>(OrderResponse.from(orderService.updateOrder(request)),HttpStatus.OK);
    }
}
