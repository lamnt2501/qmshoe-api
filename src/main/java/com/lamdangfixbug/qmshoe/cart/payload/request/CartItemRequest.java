package com.lamdangfixbug.qmshoe.cart.payload.request;

import com.lamdangfixbug.qmshoe.cart.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemRequest {
    private String sku;
    private int quantity;

    public static CartItemRequest from(final CartItem cartItem) {
        return CartItemRequest.builder().sku(cartItem.getId().getSku()).quantity(cartItem.getQuantity()).build();
    }
}
