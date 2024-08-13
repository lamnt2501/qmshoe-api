package com.lamdangfixbug.qmshoe.order.payload.request;

import com.lamdangfixbug.qmshoe.payment.entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private String voucher;
    private OrderItemRequest[] items;
    private AddressRequest address;
    private int addressId;
    private String phoneNumber;
    private PaymentMethod paymentMethod;
}
