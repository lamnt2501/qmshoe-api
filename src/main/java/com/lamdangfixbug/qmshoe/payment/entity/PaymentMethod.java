package com.lamdangfixbug.qmshoe.payment.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class PaymentMethod {
    private String name;
    private String provider;
}
