package com.lamdangfixbug.qmshoe.payment.controller;

import com.lamdangfixbug.qmshoe.configurations.VNPayConfig;
import com.lamdangfixbug.qmshoe.order.service.OrderService;
import com.lamdangfixbug.qmshoe.payment.service.PaymentService;
import com.lamdangfixbug.qmshoe.payment.service.VNPayService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final VNPayConfig vnPayConfig;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final VNPayService vnPayService;

    public PaymentController(VNPayConfig vnPayConfig, OrderService orderService, PaymentService paymentService, VNPayService vnPayService) {
        this.vnPayConfig = vnPayConfig;
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.vnPayService = vnPayService;
    }

    @GetMapping("/ipn-return")
    public Map<String, String> ipnReturn(HttpServletRequest request) throws IOException {
        return vnPayService.ipnVNPay(request);
    }
}
