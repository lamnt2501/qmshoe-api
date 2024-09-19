package com.lamdangfixbug.qmshoe.cart.payload.response;

import com.lamdangfixbug.qmshoe.cart.entity.Cart;
import com.lamdangfixbug.qmshoe.cart.entity.CartItem;
import com.lamdangfixbug.qmshoe.cart.payload.request.CartItemRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private int id;
    private List<CartItemResponse> items;
}
