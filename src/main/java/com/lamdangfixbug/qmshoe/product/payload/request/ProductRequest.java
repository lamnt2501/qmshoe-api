package com.lamdangfixbug.qmshoe.product.payload.request;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private int[] categoryId;
    private int brandId;
}
