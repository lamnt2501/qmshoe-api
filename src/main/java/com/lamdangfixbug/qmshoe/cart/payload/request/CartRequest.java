package com.lamdangfixbug.qmshoe.cart.payload.request;

import com.lamdangfixbug.qmshoe.cart.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
    private CartItemRequest[] items;
}