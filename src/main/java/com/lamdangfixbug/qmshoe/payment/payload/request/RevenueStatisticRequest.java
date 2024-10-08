package com.lamdangfixbug.qmshoe.payment.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevenueStatisticRequest {
    private String type;
    private int year;
    private int month;
    private int day;
}
