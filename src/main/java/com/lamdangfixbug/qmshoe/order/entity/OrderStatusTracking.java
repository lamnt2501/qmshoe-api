package com.lamdangfixbug.qmshoe.order.entity;

import jakarta.persistence.*;
import lombok.*;

import java.security.Principal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_status_tracking")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderStatusTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
//    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private int orderId;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String message;
    private LocalDateTime changedAt;

    @PrePersist
    protected void onCreate() {
        this.changedAt = LocalDateTime.now();
    }
}
