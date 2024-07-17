package com.lamdangfixbug.qmshoe.order.payload.response;

import com.lamdangfixbug.qmshoe.order.entity.OrderStatus;
import com.lamdangfixbug.qmshoe.order.entity.OrderStatusTracking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrackingResponse {
    private OrderStatus status;
    private String message;
    private LocalDateTime time;

    public static TrackingResponse from(OrderStatusTracking orderStatusTracking) {
        return TrackingResponse.builder()
                .message(orderStatusTracking.getMessage())
                .status(orderStatusTracking.getStatus())
                .time(orderStatusTracking.getChangedAt())
                .build();
    }
}
