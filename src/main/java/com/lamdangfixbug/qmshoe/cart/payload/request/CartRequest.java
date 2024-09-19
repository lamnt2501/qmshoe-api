package com.lamdangfixbug.qmshoe.cart.payload.request;

import com.lamdangfixbug.qmshoe.cart.entity.CartItem;
import lombok.Data;

import java.util.List;

@Data
public class CartRequest {
    private CartItemRequest[] items;
}