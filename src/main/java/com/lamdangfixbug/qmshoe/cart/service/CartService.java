package com.lamdangfixbug.qmshoe.cart.service;

import com.lamdangfixbug.qmshoe.cart.payload.request.CartRequest;
import com.lamdangfixbug.qmshoe.cart.payload.response.CartResponse;

public interface CartService {
    CartResponse getCartByCustomer();
    CartResponse updateCart(CartRequest cartRequest);
}
