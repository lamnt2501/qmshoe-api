package com.lamdangfixbug.qmshoe.product.payload.request;

import lombok.Data;

@Data
public class RatingRequest {
    private int rating;
    private String comment;
    private int productId;
}
