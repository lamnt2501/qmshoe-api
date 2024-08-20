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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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
        Cart cart = getCartByCustomerId(customer.getId());
        return CartResponse
                .builder()
                .id(cart.getId())
                .items(buildCartItemResponses(cart))
                .build();
    }

    @Override
    public CartResponse updateCart(CartRequest cartRequest) {
        Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cart cart = getCartByCustomerId(customer.getId());
        List<CartItem> cartItems = cart.getItems();
        for (CartItemRequest cir : cartRequest.getItems()) {
            CartItem cartItem = cartItemRepository.findById(CartItemPK.builder().cartId(cart.getId()).sku(cir.getSku()).build()).orElse(null);
            if (cartItem == null) {
                if (cir.getQuantity() != 0) {
                    CartItem newCartItem = CartItem.builder().quantity(cir.getQuantity()).id(CartItemPK.builder().cartId(cart.getId()).sku(cir.getSku()).build()).build();
                    cartItemRepository.save(newCartItem);
                    cartItems.add(newCartItem);
                }
            } else {
                if (cir.getQuantity() != 0) {
                    cartItems.stream().filter(ci ->
                            ci.getId().getCartId() == cart.getId() && ci.getId().getSku().equals(cartItem.getId().getSku())
                    ).toList().getFirst().setQuantity(cir.getQuantity());
                    cartItem.setQuantity(cir.getQuantity());
                    cartItemRepository.save(cartItem);
                } else {
                    cartItems.remove(cartItem);
                    cartItemRepository.delete(cartItem);
                }
            }
        }
        cart.setItems(cartItems);
        return CartResponse.builder().id(cart.getId()).items(buildCartItemResponses(cart)).build();
    }

    private List<CartItemResponse> buildCartItemResponses(Cart cart) {
        List<CartItemResponse> cartItemResponses = new ArrayList<>();
        for (CartItem ci : cart.getItems()) {
            ProductOption po = productOptionRepository.findBySku(ci.getId().getSku()).orElseThrow(() -> new ResourceNotFoundException("Product option not found"));
            cartItemResponses.add(CartItemResponse.builder()
                    .name(po.getProduct().getName())
                    .imageUrl(po.getProduct().getProductImages().stream().filter(i -> Objects.equals(i.getColor().getId(), po.getColor().getId())).toList().getFirst().getUrl())
                    .price(po.getPrice())
                    .color(po.getColor().getName())
                    .size(po.getSize().getSize())
                    .quantity(ci.getQuantity())
                    .sku(po.getSku())
                    .slug(po.getProduct().getSlug())
                    .build());
        }
        return cartItemResponses;
    }

    private Cart getCartByCustomerId(int customerId) {
        return cartRepository.findByCustomerId(customerId).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
    }

}
