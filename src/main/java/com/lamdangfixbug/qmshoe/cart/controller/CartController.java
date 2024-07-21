package com.lamdangfixbug.qmshoe.cart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamdangfixbug.qmshoe.cart.payload.request.CartItemRequest;
import com.lamdangfixbug.qmshoe.cart.payload.request.CartRequest;
import com.lamdangfixbug.qmshoe.cart.payload.response.CartResponse;
import com.lamdangfixbug.qmshoe.cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    @GetMapping
    public ResponseEntity<CartResponse> getCart(){
        return ResponseEntity.ok(cartService.getCartByCustomer());
    }

    @PutMapping
    public ResponseEntity<CartResponse> updateCart(@RequestBody String request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        CartItemRequest[] cartItemRequest = mapper.readValue(request, CartItemRequest[].class);
        CartRequest cartRequest = new CartRequest();
        cartRequest.setItems(cartItemRequest);
        return ResponseEntity.ok(cartService.updateCart(cartRequest));
    }
}
