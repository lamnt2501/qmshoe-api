package com.lamdangfixbug.qmshoe.payment.controller;

import com.lamdangfixbug.qmshoe.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/management/payments")
public class PaymentManagementController {
    private final PaymentService paymentService;

    public PaymentManagementController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/gateway-statistic")
    public ResponseEntity<?> getPaymentGateWayUsageRate(){
        return ResponseEntity.ok(paymentService.getPaymentGateWayUsageRate());
    }
}
