package com.lamdangfixbug.qmshoe.cart.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

@Entity
@Table(name = "carts")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int customerId;
    @OneToMany(mappedBy = "id.cartId")
    private List<CartItem> items;
}
