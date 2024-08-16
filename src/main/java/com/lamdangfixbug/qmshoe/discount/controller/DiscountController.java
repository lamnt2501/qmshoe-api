package com.lamdangfixbug.qmshoe.discount.controller;

import com.lamdangfixbug.qmshoe.discount.entity.Discount;
import com.lamdangfixbug.qmshoe.discount.payload.response.DiscountResponse;
import com.lamdangfixbug.qmshoe.discount.service.DiscountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/discounts")
public class DiscountController {
    private final DiscountService discountService;
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping()
    public ResponseEntity<List<DiscountResponse>> getDiscounts() {
        return ResponseEntity.ok(discountService.getAllOrderDiscounts().stream().map(DiscountResponse::from).toList());
    }

}
