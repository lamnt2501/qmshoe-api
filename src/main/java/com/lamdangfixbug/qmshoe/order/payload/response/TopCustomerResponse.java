package com.lamdangfixbug.qmshoe.order.payload.response;

import com.lamdangfixbug.qmshoe.order.entity.TopCustomer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopCustomerResponse {
    private String name;
    private String memberShipClass;
    private double spend;
    private String avtUrl;

    public static TopCustomerResponse from(final TopCustomer topCustomer) {
        return TopCustomerResponse.builder()
                .name(topCustomer.getCustomer().getName())
                .memberShipClass(topCustomer.getMemberShipClass())
                .spend(topCustomer.getSpend())
                .avtUrl(topCustomer.getCustomer().getAvtUrl())
                .build();
    }
}
