package com.lamdangfixbug.qmshoe.order.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
    private String city;
    private String district;
    private String ward;
    private String specificAddress;
}
