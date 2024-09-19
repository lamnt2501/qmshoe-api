package com.lamdangfixbug.qmshoe.payment.service.impl;

import com.lamdangfixbug.qmshoe.exceptions.ResourceNotFoundException;
import com.lamdangfixbug.qmshoe.exceptions.UpdatePaymentException;
import com.lamdangfixbug.qmshoe.order.entity.OrderStatus;
import com.lamdangfixbug.qmshoe.order.entity.TopCustomer;
import com.lamdangfixbug.qmshoe.order.payload.request.OrderRequest;
import com.lamdangfixbug.qmshoe.order.payload.request.UpdateOrderStatusRequest;
import com.lamdangfixbug.qmshoe.order.repository.TopCustomerRepository;
import com.lamdangfixbug.qmshoe.order.service.OrderService;
import com.lamdangfixbug.qmshoe.payment.entity.PaymentDetails;
import com.lamdangfixbug.qmshoe.payment.entity.PaymentStatus;
import com.lamdangfixbug.qmshoe.payment.payload.request.RevenueStatisticRequest;
import com.lamdangfixbug.qmshoe.payment.payload.request.UpdatePaymentRequest;
import com.lamdangfixbug.qmshoe.payment.repository.PaymentDetailsRepository;
import com.lamdangfixbug.qmshoe.payment.service.PaymentService;
import com.lamdangfixbug.qmshoe.user.entity.Customer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentDetailsRepository paymentDetailsRepository;
    private final OrderService orderService;
    private final TopCustomerRepository topCustomerRepository;

    public PaymentServiceImpl(PaymentDetailsRepository paymentDetailsRepository, OrderService orderService, TopCustomerRepository topCustomerRepository) {
        this.paymentDetailsRepository = paymentDetailsRepository;
        this.orderService = orderService;
        this.topCustomerRepository = topCustomerRepository;
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
    public void updatePayment(UpdatePaymentRequest pd) {
        PaymentDetails paymentDetails = paymentDetailsRepository.findById(pd.getId()).orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if(pd.getStatus().getLevel() <= paymentDetails.getStatus().getLevel()) {
            throw new UpdatePaymentException("Invalid payment status");
        }

        paymentDetails.setStatus(pd.getStatus());

        if(pd.getStatus() == PaymentStatus.PAID) {
            Customer customer = (orderService.getOrderAdmin(paymentDetails.getOrderId()).getCustomer());
            TopCustomer c = topCustomerRepository.findTopCustomerByCustomer_Id(customer.getId()).orElse(null);
            if (c != null) {
                c.setSpend(c.getSpend() + paymentDetails.getAmount());
                c.setMemberShipClass(memberShipClassCalculate(c.getSpend()));
            } else {
                c = TopCustomer.builder().customer(customer).memberShipClass(memberShipClassCalculate(paymentDetails.getAmount()))
                        .spend(paymentDetails.getAmount()).build();
            }
            topCustomerRepository.save(c);
        }
        paymentDetailsRepository.save(paymentDetails);
    }
    private String memberShipClassCalculate(double amount) {
        if (amount > 50000000) return "Diamond";
        if (amount > 20000000) return "Emerald";
        if (amount > 10000000) return "Golden";
        if (amount > 5000000) return "Silver";
        if (amount > 2000000) return "Bronze";
        return "";
    }
    @Override
    public Object getPaymentGateWayUsageRate() {
        List<?> queryRes = paymentDetailsRepository.getPaymentGateWayUsageRate();
        Map<Object,Object> res = new HashMap<>();
        queryRes.stream().map(v->(Object[]) v).forEach(v->res.put(v[0],v[1]));
        return res;
    }

    @Override
    public Object getRevenue(RevenueStatisticRequest request) {
        Map<Object,Object> res = new HashMap<>();
        switch (request.getType()){
            case "year" -> paymentDetailsRepository.getRevenue(request.getYear()).stream().map(v->(Object[])v).forEach(v->res.put(Month.of((Integer) v[0]),v[1]));
            case "month"-> {}
            default -> throw new IllegalStateException("Unexpected value: " + request.getType());
        };
        return res;
    }
}
