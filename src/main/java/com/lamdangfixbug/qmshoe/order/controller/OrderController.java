package com.lamdangfixbug.qmshoe.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamdangfixbug.qmshoe.order.entity.Order;
import com.lamdangfixbug.qmshoe.order.payload.request.OrderRequest;
import com.lamdangfixbug.qmshoe.order.payload.request.UpdateOrderStatusRequest;
import com.lamdangfixbug.qmshoe.order.payload.response.OrderResponse;
import com.lamdangfixbug.qmshoe.order.service.OrderService;
import com.lamdangfixbug.qmshoe.payment.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {
    private final OrderService orderService;
    private final VNPayService vnPayService;

    public OrderController(OrderService orderService, VNPayService vnPayService) {
        this.orderService = orderService;
        this.vnPayService = vnPayService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrder(@RequestParam Map<String, Object> params) {
        return ResponseEntity.ok(orderService.getAllOrders(params).stream().map(OrderResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(OrderResponse.from(orderService.getOrder(id)));
    }

    @PostMapping
    public ResponseEntity<Map<String,String>> createOrder(@RequestBody String order, HttpServletRequest request) throws IOException {
        OrderRequest orderRequest;
        ObjectMapper mapper = new ObjectMapper();
        orderRequest = mapper.readValue(order, OrderRequest.class);
        Order createdOrder = orderService.placeOrder(orderRequest);
        Map<String,String> response = new HashMap<>();

        if (!orderRequest.getPaymentMethod().getName().equalsIgnoreCase("COD")) {

            String paymentUrl = vnPayService.getPaymentUrl(
                   createdOrder,
                    request.getRemoteAddr());
            response.put("Message","Redirect to checkout");
            response.put("PaymentUrl", paymentUrl);
            response.put("StatusCode", String.valueOf(HttpStatus.TEMPORARY_REDIRECT.value()));
        }else {
            response.put("Message", "Created Order Successfully");
            response.put("StatusCode", String.valueOf(HttpStatus.OK.value()));
        }
        return ResponseEntity.ok(response);
    }

    @PatchMapping
    public ResponseEntity<OrderResponse> updateOrder(@RequestBody UpdateOrderStatusRequest request) {
        return new ResponseEntity<>(OrderResponse.from(orderService.updateOrder(request)), HttpStatus.OK);
    }
}
