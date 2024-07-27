package com.lamdangfixbug.qmshoe.payment.service.impl;

import com.lamdangfixbug.qmshoe.exceptions.ResourceNotFoundException;
import com.lamdangfixbug.qmshoe.order.entity.OrderStatus;
import com.lamdangfixbug.qmshoe.order.payload.request.OrderRequest;
import com.lamdangfixbug.qmshoe.order.payload.request.UpdateOrderStatusRequest;
import com.lamdangfixbug.qmshoe.order.service.OrderService;
import com.lamdangfixbug.qmshoe.payment.entity.PaymentDetails;
import com.lamdangfixbug.qmshoe.payment.entity.PaymentStatus;
import com.lamdangfixbug.qmshoe.payment.repository.PaymentDetailsRepository;
import com.lamdangfixbug.qmshoe.payment.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentDetailsRepository paymentDetailsRepository;
    private final OrderService orderService;

    public PaymentServiceImpl(PaymentDetailsRepository paymentDetailsRepository, OrderService orderService) {
        this.paymentDetailsRepository = paymentDetailsRepository;
        this.orderService = orderService;
    }

    @Override
    public void createPayment(OrderRequest orderRequest) {

    }

    @Override
    public PaymentDetails getPayment(int paymentId) {
        return paymentDetailsRepository.findById(paymentId).orElseThrow(()->new ResourceNotFoundException("Payment not found"));
    }

    @Override
    @Transactional
    public void updatePayment(PaymentDetails pd) {
        PaymentDetails paymentDetails = paymentDetailsRepository.findById(pd.getId()).orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        paymentDetails.setStatus(pd.getStatus());
        if (pd.getStatus() == PaymentStatus.CANCELLED) {
            orderService.updateOrderAdmin(UpdateOrderStatusRequest.builder()
                    .id(paymentDetails.getOrderId())
                    .status(OrderStatus.CANCEL)
                    .message("Payment failed")
                    .build());
        }
        paymentDetails.setTransactionIdRef(pd.getTransactionIdRef());
        paymentDetailsRepository.save(paymentDetails);
    }
}
