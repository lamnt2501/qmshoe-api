package com.lamdangfixbug.qmshoe.cart.payload.response;

import com.lamdangfixbug.qmshoe.product.entity.ProductOption;
import com.lamdangfixbug.qmshoe.product.payload.response.ProductOptionResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private String name;
    private String imageUrl;
    private double price;
    private String color;
    private String size;
    private int quantity;
}
