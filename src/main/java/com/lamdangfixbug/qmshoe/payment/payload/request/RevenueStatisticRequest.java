package com.lamdangfixbug.qmshoe.payment.payload.request;

import lombok.Data;

@Data
public class RevenueStatisticRequest {
    private String type;
    private int year;
    private int month;
    private int day;
}
