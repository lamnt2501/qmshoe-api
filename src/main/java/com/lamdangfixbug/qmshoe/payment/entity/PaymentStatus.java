package com.lamdangfixbug.qmshoe.payment.entity;

public enum PaymentStatus {
    UNPAID(0),
    PAID(1),
    PROCESSING(0),
    CANCELLED(1);
    private int level;
    PaymentStatus(int level){
        this.level = level;
    }

    public int getLevel(){
        return level;
    }
}
