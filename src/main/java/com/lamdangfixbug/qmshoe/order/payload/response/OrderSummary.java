package com.lamdangfixbug.qmshoe.order.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummary {
    private int totalOrder;
    private int todayOrder;
    private int successfulOrder;
    private int cancelledOrder;
    private int pendingOrder;
}
