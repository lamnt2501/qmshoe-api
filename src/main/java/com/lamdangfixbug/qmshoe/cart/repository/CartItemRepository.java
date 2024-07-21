package com.lamdangfixbug.qmshoe.cart.repository;

import com.lamdangfixbug.qmshoe.cart.entity.CartItem;
import com.lamdangfixbug.qmshoe.cart.entity.embedded.CartItemPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemPK> {
    @Query(value = "insert into qmshoes.cart_items (cart_id, quantity, sku) values (:cartId,:quantity,:sku)",nativeQuery = true)
    void saveNative(int cartId, int quantity, String sku);
}