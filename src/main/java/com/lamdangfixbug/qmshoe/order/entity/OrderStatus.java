package com.lamdangfixbug.qmshoe.order.entity;

public enum OrderStatus {
    WAITING(0),
    APPROVED(1),
    SHIPPING(2),
    SUCCEEDED(3),
    CANCEL(3);


    private int priority;
    public int getPriority() {
        return priority;
    }
    OrderStatus(int priority){
        this.priority = priority;
    }


}
