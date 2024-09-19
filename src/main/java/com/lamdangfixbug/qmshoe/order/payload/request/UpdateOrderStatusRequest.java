package com.lamdangfixbug.qmshoe.order.payload.request;

import com.lamdangfixbug.qmshoe.order.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStatusRequest {
    private int id;
    private OrderStatus status;
    private String message;
}
