package com.lamdangfixbug.qmshoe.product.payload.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DiscountRequest {
    private String name;
    private double value;
    private double maxDiscount;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private int[] productIds;
}
