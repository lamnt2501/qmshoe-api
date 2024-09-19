package com.lamdangfixbug.qmshoe.discount.payload.response;

import com.lamdangfixbug.qmshoe.discount.entity.Discount;
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
    private double maxUsage;

    public static DiscountResponse from(final Discount discount) {
        return DiscountResponse.builder()
                .name(discount.getName())
                .value(discount.getValue())
                .maxUsage(discount.getMaxUsage())
                .build();
    }
}
