package com.lamdangfixbug.qmshoe.order.entity.embedded;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lamdangfixbug.qmshoe.order.entity.Order;
import com.lamdangfixbug.qmshoe.product.entity.ProductOption;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemPK implements Serializable {
    private int orderId;
    private String sku;
}
