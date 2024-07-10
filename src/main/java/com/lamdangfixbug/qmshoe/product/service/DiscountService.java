package com.lamdangfixbug.qmshoe.product.service;

import com.lamdangfixbug.qmshoe.product.entity.Discount;
import com.lamdangfixbug.qmshoe.product.payload.request.DiscountRequest;

import java.util.List;

public interface DiscountService {
    Discount createDiscount(DiscountRequest discountRequest);
    Discount getDiscountById(int id);
    Discount updateDiscount(DiscountRequest discountRequest);
    List<Discount> getAllDiscounts();

}
