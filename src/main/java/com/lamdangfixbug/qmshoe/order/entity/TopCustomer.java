package com.lamdangfixbug.qmshoe.order.entity;

import com.lamdangfixbug.qmshoe.user.entity.Customer;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "top_customer")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "customer_id",referencedColumnName = "id")
    private Customer customer;
    private double spend;
    private String memberShipClass;
}
