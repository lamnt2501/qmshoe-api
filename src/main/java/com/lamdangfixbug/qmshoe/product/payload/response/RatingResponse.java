package com.lamdangfixbug.qmshoe.product.payload.response;

import com.lamdangfixbug.qmshoe.product.entity.Rating;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponse {
    private int id;
    private double rating;
    private String comment;
    private String by;
    private String avtUrl;
    private LocalDateTime createdAt;

    public static RatingResponse from(final Rating rating) {
        return RatingResponse.builder()
                .id(rating.getId())
                .rating(rating.getRatingValue())
                .comment(rating.getComment())
                .by(rating.getCustomer().getName())
                .avtUrl(rating.getCustomer().getAvtUrl())
                .createdAt(rating.getCreatedAt())
                .build();
    }
}
