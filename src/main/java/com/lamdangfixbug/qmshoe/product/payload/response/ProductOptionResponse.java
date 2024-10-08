package com.lamdangfixbug.qmshoe.product.payload.response;

import com.lamdangfixbug.qmshoe.product.entity.ProductOption;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionResponse {
    private String sku;
    private String color;
    private String size;
    private int quantity;
    private  double price;
    private LocalDateTime createdAt;

    public static ProductOptionResponse from(final ProductOption productOption) {
        return ProductOptionResponse.builder()
                .sku(productOption.getSku())
                .color(productOption.getColor().getName())
                .size(productOption.getSize().getSize())
                .quantity(productOption.getQuantity())
                .createdAt(productOption.getCreatedAt())
                .price(productOption.getPrice())
                .build();
    }
}
