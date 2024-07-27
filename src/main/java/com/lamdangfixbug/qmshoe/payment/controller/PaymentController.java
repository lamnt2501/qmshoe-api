package com.lamdangfixbug.qmshoe.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamdangfixbug.qmshoe.configurations.VNPayConfig;
import com.lamdangfixbug.qmshoe.order.entity.Order;
import com.lamdangfixbug.qmshoe.order.entity.OrderStatus;
import com.lamdangfixbug.qmshoe.order.service.OrderService;
import com.lamdangfixbug.qmshoe.payment.entity.PaymentDetails;
import com.lamdangfixbug.qmshoe.payment.entity.PaymentStatus;
import com.lamdangfixbug.qmshoe.payment.service.PaymentService;
import com.lamdangfixbug.qmshoe.payment.service.VNPayService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final VNPayConfig vnPayConfig;
    private final OrderService orderService;
    private final PaymentService paymentService;

    public PaymentController(VNPayConfig vnPayConfig, OrderService orderService, PaymentService paymentService) {
        this.vnPayConfig = vnPayConfig;
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping("/ipn-return")
    public Map<String, String> ipnReturn(HttpServletRequest request) throws IOException {
        Map<String, String> response = new HashMap<>();
        String rspCode = "";
        String message = "";
        try {
            Map<String, String> fields = new HashMap<>();
            for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
                String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII);
                String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII);
                if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            fields.remove("vnp_SecureHashType");
            fields.remove("vnp_SecureHash");
            String signValue = vnPayConfig.hashAllFields(fields);


            if (signValue.equals(vnp_SecureHash)) {
                boolean checkOrderId = orderService.orderExists(Integer.parseInt(request.getParameter("vnp_TxnRef")));
                if (checkOrderId) {
                    Order order = orderService.getOrderAdmin(Integer.parseInt(request.getParameter("vnp_TxnRef")));
                    boolean checkAmount = (Integer.parseInt(request.getParameter("vnp_Amount")) / 100) == (int) order.getTotal();
                    if (checkAmount) {
                        boolean checkOrderStatus = order.getStatus() == OrderStatus.WAITING;
                        if (checkOrderStatus) {
                            PaymentDetails paymentDetails = order.getPaymentDetails();
                            paymentDetails.setTransactionIdRef(request.getParameter("vnp_TransactionNo"));
                            if ("00".equals(request.getParameter("vnp_ResponseCode"))) {
                                paymentDetails.setStatus(PaymentStatus.PAID);
                            } else {
                                paymentDetails.setStatus(PaymentStatus.CANCELLED);
                            }
                            paymentService.updatePayment(paymentDetails);
                            rspCode = "00";
                            message = "Confirm Success";
                        } else {
                            rspCode = "02";
                            message = "Order already confirmed";
                        }
                    } else {
                        rspCode = "04";
                        message = "Invalid Amount";
                    }
                } else {
                    rspCode = "01";
                    message = "Order not Found";
                }
            } else {
                rspCode = "97";
                message = "Invalid Checksum";
            }
        } catch (Exception e) {
            rspCode = "99";
            message = "Unknown error";
        }
        response.put("RspCode", rspCode);
        response.put("Message", message);
        return response;
    }
}
