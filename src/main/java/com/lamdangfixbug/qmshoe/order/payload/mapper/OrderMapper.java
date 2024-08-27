package com.lamdangfixbug.qmshoe.order.payload.mapper;

import com.lamdangfixbug.qmshoe.order.entity.Order;
import com.lamdangfixbug.qmshoe.order.payload.request.AddressRequest;
import com.lamdangfixbug.qmshoe.order.payload.request.OrderItemRequest;
import com.lamdangfixbug.qmshoe.order.payload.response.OrderResponse;
import com.lamdangfixbug.qmshoe.order.payload.response.TrackingResponse;
import com.lamdangfixbug.qmshoe.user.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {
    private OrderItemMapper orderItemMapper;

    public OrderMapper(OrderItemMapper orderItemMapper) {
        this.orderItemMapper = orderItemMapper;
    }

    public OrderResponse map(final Order order) {
        Address a = order.getAddress();

        return OrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .phoneNumber(order.getPhoneNumber())
                .receiverName(order.getReceiverName())
                .paymentStatus(order.getPaymentDetails().getStatus().name())
                .createdAt(order.getCreatedAt())
                .customerName(order.getCustomer().getName())
                .email(order.getCustomer().getEmail())
                .paymentMethod(order.getPaymentDetails().getPaymentMethod())
                .items(order
                        .getOrderItems()
                        .stream()
                        .map(
                                oi -> orderItemMapper.map(oi)
                        ).toList()
                )
                .total(order.getTotal())
                .address(AddressRequest.builder().specificAddress(a.getSpecificAddress()).city(a.getCity())
                        .district(a.getDistrict()).ward(a.getDistrict()).build())
                .tracking(order.getOrderStatusTracking().stream().map(TrackingResponse::from).toList())
                .build();
    }
}
