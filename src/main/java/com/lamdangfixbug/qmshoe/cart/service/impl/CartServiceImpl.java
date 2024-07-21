package com.lamdangfixbug.qmshoe.cart.service.impl;

import com.lamdangfixbug.qmshoe.cart.entity.Cart;
import com.lamdangfixbug.qmshoe.cart.entity.CartItem;
import com.lamdangfixbug.qmshoe.cart.entity.embedded.CartItemPK;
import com.lamdangfixbug.qmshoe.cart.payload.request.CartItemRequest;
import com.lamdangfixbug.qmshoe.cart.payload.request.CartRequest;
import com.lamdangfixbug.qmshoe.cart.payload.response.CartResponse;
import com.lamdangfixbug.qmshoe.cart.repository.CartItemRepository;
import com.lamdangfixbug.qmshoe.cart.repository.CartRepository;
import com.lamdangfixbug.qmshoe.cart.service.CartService;
import com.lamdangfixbug.qmshoe.exceptions.ResourceNotFoundException;
import com.lamdangfixbug.qmshoe.user.entity.Customer;
import kotlin.jvm.internal.SerializedIr;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public CartResponse getCartByCustomer() {
        Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cart cart = cartRepository.findByCustomerId(customer.getId()).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        return CartResponse.from(cart);
    }

    @Override
    public CartResponse updateCart(CartRequest cartRequest) {
        Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cart cart = cartRepository.findByCustomerId(customer.getId()).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        List<CartItem> newItems = new ArrayList<>();
        for (CartItemRequest cir : cartRequest.getItems()) {
            newItems.add(
                    CartItem.builder().id(
                                    CartItemPK.builder().sku(cir.getSku()).cartId(cart.getId()).build()
                            )
                            .quantity(cir.getQuantity()).build());

        }
        cartItemRepository.deleteAll();
        cartItemRepository.saveAll(newItems);
        cart.setItems(newItems);
        return CartResponse.from(cartRepository.save(cart));
    }
}
