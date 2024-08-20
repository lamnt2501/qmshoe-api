package com.lamdangfixbug.qmshoe.order.payload.response;

import com.lamdangfixbug.qmshoe.order.entity.Order;
import com.lamdangfixbug.qmshoe.order.entity.OrderItem;
import com.lamdangfixbug.qmshoe.order.entity.OrderStatus;
import com.lamdangfixbug.qmshoe.order.entity.OrderStatusTracking;
import com.lamdangfixbug.qmshoe.order.payload.request.AddressRequest;
import com.lamdangfixbug.qmshoe.order.payload.request.OrderItemRequest;
import com.lamdangfixbug.qmshoe.user.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private int orderId;
    private String status;
    private double total;
    private List<OrderItemRequest> items;
    private String address;
    private String phoneNumber;
    private String receiverName;
    private String paymentStatus;
    private List<TrackingResponse> tracking;

    public static OrderResponse from(final Order order) {
        Address a = order.getAddress();
        return OrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .phoneNumber(order.getPhoneNumber())
                .receiverName(order.getReceiverName())
                .paymentStatus(order.getPaymentDetails().getStatus().name())
                .items(order
                        .getOrderItems()
                        .stream()
                        .map(
                                oi -> OrderItemRequest.builder().sku(oi.getId().getSku()).quantity(oi.getQuantity()).build()
                        ).toList()
                )
                .total(order.getTotal())
                .address(String.join(", ",a.getSpecificAddress(),a.getDistrict(),a.getCity()))
                .tracking(order.getOrderStatusTracking().stream().map(TrackingResponse::from).toList())
                .build();
    }
}
