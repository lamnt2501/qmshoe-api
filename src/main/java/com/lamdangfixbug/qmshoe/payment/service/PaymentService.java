package com.lamdangfixbug.qmshoe.payment.service;

import com.lamdangfixbug.qmshoe.order.payload.request.OrderRequest;
import com.lamdangfixbug.qmshoe.payment.entity.PaymentDetails;
import com.lamdangfixbug.qmshoe.payment.payload.request.RevenueStatisticRequest;
import com.lamdangfixbug.qmshoe.payment.payload.request.UpdatePaymentRequest;

import java.time.LocalDateTime;

public interface PaymentService {
    void createPayment(OrderRequest orderRequest);
    PaymentDetails getPayment(int paymentId);
    void updatePayment(UpdatePaymentRequest updatePaymentRequest);
    Object getPaymentGateWayUsageRate();
    Object getRevenue(RevenueStatisticRequest request);
}
