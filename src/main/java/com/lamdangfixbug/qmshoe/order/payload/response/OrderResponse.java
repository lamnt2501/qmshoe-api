package com.lamdangfixbug.qmshoe.order.payload.response;

import com.lamdangfixbug.qmshoe.order.entity.Order;
import com.lamdangfixbug.qmshoe.order.entity.OrderItem;
import com.lamdangfixbug.qmshoe.order.entity.OrderStatus;
import com.lamdangfixbug.qmshoe.order.payload.request.OrderItemRequest;
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

    public static OrderResponse from(final Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus().name())
                .items(order
                        .getOrderItems()
                        .stream()
                        .map(
                                oi -> OrderItemRequest.builder().sku(oi.getId().getSku()).quantity(oi.getQuantity()).build()
                        ).toList()
                )
                .total(order.getTotal())
                .build();
    }
}
