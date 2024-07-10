package com.lamdangfixbug.qmshoe.product.payload.response;

import com.lamdangfixbug.qmshoe.product.entity.Discount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountResponse {
    private String name;
    private double value;
    private double maxDiscount;

    public static DiscountResponse from(final Discount discount) {
        return DiscountResponse.builder()
                .name(discount.getName())
                .value(discount.getValue())
                .maxDiscount(discount.getMaxDiscount())
                .build();
    }
}
