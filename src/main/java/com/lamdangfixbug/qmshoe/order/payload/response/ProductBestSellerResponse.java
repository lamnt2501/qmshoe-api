package com.lamdangfixbug.qmshoe.order.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductBestSellerResponse {
    private String sku;
    private double price;
    private String name;
    private String imageUrl;
    private int sold;
}
