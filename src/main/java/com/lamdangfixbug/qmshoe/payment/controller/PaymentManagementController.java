package com.lamdangfixbug.qmshoe.payment.controller;

import com.lamdangfixbug.qmshoe.payment.payload.request.RevenueStatisticRequest;
import com.lamdangfixbug.qmshoe.payment.payload.request.UpdatePaymentRequest;
import com.lamdangfixbug.qmshoe.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/management/payments")
public class PaymentManagementController {
    private final PaymentService paymentService;

    public PaymentManagementController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/gateway-statistic")
    public ResponseEntity<?> getPaymentGateWayUsageRate() {
        return ResponseEntity.ok(paymentService.getPaymentGateWayUsageRate());
    }

    @PatchMapping()
    public ResponseEntity<?> updatePayment(@RequestBody  UpdatePaymentRequest request) {
        paymentService.updatePayment(request);
        Map<String, String> res = new HashMap<>();
        res.put("StatusCode", "200");
        res.put("Message", "Update Success");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/revenue")
    public  ResponseEntity<?> getRevenue(@RequestBody RevenueStatisticRequest request){
        return ResponseEntity.ok(paymentService.getRevenue(request));
    }
}
