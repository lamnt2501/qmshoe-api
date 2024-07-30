package com.lamdangfixbug.qmshoe.product.payload.response;

import com.lamdangfixbug.qmshoe.product.entity.Rating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponse {
    private int id;
    private double rating;
    private String comment;
    private String by;

    public static RatingResponse from(final Rating rating) {
        return RatingResponse.builder()
                .id(rating.getId())
                .rating(rating.getRatingValue())
                .comment(rating.getComment())
                .by(rating.getCustomer().getName())
                .build();
    }
}
