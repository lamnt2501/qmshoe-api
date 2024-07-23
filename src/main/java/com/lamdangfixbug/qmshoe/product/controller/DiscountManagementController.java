package com.lamdangfixbug.qmshoe.product.controller;

import com.lamdangfixbug.qmshoe.product.entity.Discount;
import com.lamdangfixbug.qmshoe.product.payload.request.DiscountRequest;
import com.lamdangfixbug.qmshoe.product.service.DiscountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/management/discounts")
public class DiscountManagementController {
    private final DiscountService discountService;

    public DiscountManagementController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping
    public ResponseEntity<List<Discount>> getAllDiscounts() {
        return new ResponseEntity<>(discountService.getAllDiscounts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Discount> getDiscountById(@PathVariable Integer id) {
        return new ResponseEntity<>(discountService.getDiscountById(id),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Discount> createDiscount(@RequestBody DiscountRequest discountRequest) {
        return new ResponseEntity<>(discountService.createDiscount(discountRequest),HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Discount> updateDiscount(@RequestBody DiscountRequest discountRequest) {
        return new ResponseEntity<>(discountService.updateDiscount(discountRequest),HttpStatus.OK);
    }
}
