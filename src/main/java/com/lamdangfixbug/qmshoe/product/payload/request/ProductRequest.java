package com.lamdangfixbug.qmshoe.product.payload.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductRequest {
    private String name;
    private String description;
    private int[] categoryId;
    private int brandId;
    private double price;
}
