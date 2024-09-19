package com.lamdangfixbug.qmshoe.discount.service;

import com.lamdangfixbug.qmshoe.discount.entity.Discount;
import com.lamdangfixbug.qmshoe.discount.payload.request.DiscountRequest;

import java.util.List;

public interface DiscountService {
    Discount createDiscount(DiscountRequest discountRequest);
    Discount getDiscountById(int id);
    Discount updateDiscount(DiscountRequest discountRequest);
    List<Discount> getAllDiscounts();
    List<Discount> getAllOrderDiscounts();
}
