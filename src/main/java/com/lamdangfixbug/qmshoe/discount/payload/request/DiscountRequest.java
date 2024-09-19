package com.lamdangfixbug.qmshoe.discount.payload.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DiscountRequest {
    private String name;
    private double value;
    private double maxUsage;
    private String type;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private boolean isOrderDiscount;
    private int[] productIds;
}
