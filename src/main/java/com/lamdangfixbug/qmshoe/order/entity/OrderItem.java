package com.lamdangfixbug.qmshoe.order.entity;

import com.lamdangfixbug.qmshoe.order.entity.embedded.OrderItemPK;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class OrderItem {
    @EmbeddedId
    private OrderItemPK id;
    private int quantity;
}
