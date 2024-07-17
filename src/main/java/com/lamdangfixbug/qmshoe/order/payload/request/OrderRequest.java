package com.lamdangfixbug.qmshoe.order.payload.request;

import com.lamdangfixbug.qmshoe.user.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private String voucher;
    private OrderItemRequest[] items;
    private AddressRequest address;
}
