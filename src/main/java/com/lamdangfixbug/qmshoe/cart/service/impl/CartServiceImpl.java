package com.lamdangfixbug.qmshoe.cart.service.impl;

import com.lamdangfixbug.qmshoe.cart.entity.Cart;
import com.lamdangfixbug.qmshoe.cart.entity.CartItem;
import com.lamdangfixbug.qmshoe.cart.entity.embedded.CartItemPK;
import com.lamdangfixbug.qmshoe.cart.payload.request.CartItemRequest;
import com.lamdangfixbug.qmshoe.cart.payload.request.CartRequest;
import com.lamdangfixbug.qmshoe.cart.payload.response.CartItemResponse;
import com.lamdangfixbug.qmshoe.cart.payload.response.CartResponse;
import com.lamdangfixbug.qmshoe.cart.repository.CartItemRepository;
import com.lamdangfixbug.qmshoe.cart.repository.CartRepository;
import com.lamdangfixbug.qmshoe.cart.service.CartService;
import com.lamdangfixbug.qmshoe.exceptions.ResourceNotFoundException;
import com.lamdangfixbug.qmshoe.product.entity.Product;
import com.lamdangfixbug.qmshoe.product.entity.ProductOption;
import com.lamdangfixbug.qmshoe.product.repository.ProductOptionRepository;
import com.lamdangfixbug.qmshoe.product.repository.ProductRepository;
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
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;

    public CartServiceImpl(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository, ProductOptionRepository productOptionRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.productOptionRepository = productOptionRepository;
    }

    @Override
    public CartResponse getCartByCustomer() {
        Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cart cart = cartRepository.findByCustomerId(customer.getId()).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        return CartResponse
                .builder()
                .id(cart.getId())
                .items(buildCartItemResponses(cart))
                .build();
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

        return CartResponse.builder().id(cart.getId()).items(buildCartItemResponses(cart)).build();
    }

    private List<CartItemResponse> buildCartItemResponses(Cart cart) {
        List<CartItemResponse> cartItemResponses = new ArrayList<>();
        for(CartItem ci :  cart.getItems()){
            ProductOption po = productOptionRepository.findBySku(ci.getId().getSku()).orElseThrow(() -> new ResourceNotFoundException("Product option not found"));
            cartItemResponses.add(CartItemResponse.builder()
                    .name(po.getProduct().getName())
                    .imageUrl(po.getProduct().getProductImages().stream().filter(i->i.getColor().getId()==po.getColor().getId()).toList().getFirst().getUrl())
                    .price(po.getProduct().getPrice())
                    .color(po.getColor().getName())
                    .size(po.getSize().getSize())
                    .quantity(ci.getQuantity())
                    .build());
        }
        return cartItemResponses;
    }
}
