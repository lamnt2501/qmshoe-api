package com.lamdangfixbug.qmshoe.order.payload.response;

import com.lamdangfixbug.qmshoe.order.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private String sku;
    private int quantity;
    private double price;
    private String size;
    private String color;
    private String imgUrl;
    private String name;
}
