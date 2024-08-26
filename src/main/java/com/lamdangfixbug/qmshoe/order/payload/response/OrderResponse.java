package com.lamdangfixbug.qmshoe.order.payload.response;

import com.lamdangfixbug.qmshoe.order.entity.Order;
import com.lamdangfixbug.qmshoe.order.entity.OrderItem;
import com.lamdangfixbug.qmshoe.order.entity.OrderStatus;
import com.lamdangfixbug.qmshoe.order.entity.OrderStatusTracking;
import com.lamdangfixbug.qmshoe.order.payload.mapper.OrderItemMapper;
import com.lamdangfixbug.qmshoe.order.payload.request.AddressRequest;
import com.lamdangfixbug.qmshoe.order.payload.request.OrderItemRequest;
import com.lamdangfixbug.qmshoe.payment.entity.PaymentMethod;
import com.lamdangfixbug.qmshoe.user.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private int orderId;
    private String status;
    private double total;
    private List<OrderItemResponse> items;
    private String address;
    private String phoneNumber;
    private String receiverName;
    private String paymentStatus;
    private PaymentMethod paymentMethod;
    private LocalDateTime createdAt;
    private String customerName;
    private String email;
    private List<TrackingResponse> tracking;

}
