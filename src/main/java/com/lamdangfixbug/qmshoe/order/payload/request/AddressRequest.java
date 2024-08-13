package com.lamdangfixbug.qmshoe.order.payload.request;

import lombok.Data;

@Data
public class AddressRequest {
    private String city;
    private String district;
    private String ward;
    private String specificAddress;
}
