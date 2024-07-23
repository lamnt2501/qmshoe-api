package com.lamdangfixbug.qmshoe.product.controller;

import com.lamdangfixbug.qmshoe.product.entity.Brand;
import com.lamdangfixbug.qmshoe.product.payload.request.BrandRequest;
import com.lamdangfixbug.qmshoe.product.service.BrandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/management/brands")
public class BrandManagementController {
    private final BrandService brandService;

    public BrandManagementController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping
    public ResponseEntity<Brand> createBrand(@RequestParam String name,
                                             @RequestParam String description,
                                             MultipartFile image) {
        return new ResponseEntity<>(brandService.createBrand(BrandRequest.builder().name(name).description(description).image(image).build()), HttpStatus.CREATED);
    }
}
