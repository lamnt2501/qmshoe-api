package com.lamdangfixbug.qmshoe.cart.entity;


import com.lamdangfixbug.qmshoe.cart.entity.embedded.CartItemPK;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Table(name = "cart_items")
@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @EmbeddedId
    private CartItemPK id;

    private int quantity;

    @Override
    public String toString() {
        return "CartItem{" +
                "sku=" + id.getSku() +
                ", quantity=" + quantity +
                '}';
    }
}
