package com.lamdangfixbug.qmshoe.payment.service;

import com.lamdangfixbug.qmshoe.configurations.VNPayConfig;
import com.lamdangfixbug.qmshoe.order.payload.request.OrderRequest;
import com.lamdangfixbug.qmshoe.payment.entity.PaymentDetails;
import com.lamdangfixbug.qmshoe.payment.entity.PaymentStatus;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public interface PaymentService {
    void createPayment(OrderRequest orderRequest);
    PaymentDetails getPayment(int paymentId);
    void updatePayment(PaymentDetails paymentDetails);
}
