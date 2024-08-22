package com.lamdangfixbug.qmshoe.payment.service;

import com.lamdangfixbug.qmshoe.order.payload.request.OrderRequest;
import com.lamdangfixbug.qmshoe.payment.entity.PaymentDetails;

public interface PaymentService {
    void createPayment(OrderRequest orderRequest);
    PaymentDetails getPayment(int paymentId);
    void updatePayment(PaymentDetails paymentDetails);
    Object getPaymentGateWayUsageRate();
}
