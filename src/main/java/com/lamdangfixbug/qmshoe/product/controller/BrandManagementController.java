package com.lamdangfixbug.qmshoe.product.controller;

import com.lamdangfixbug.qmshoe.product.entity.Brand;
import com.lamdangfixbug.qmshoe.product.payload.request.BrandRequest;
import com.lamdangfixbug.qmshoe.product.service.BrandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/management/brands")
public class BrandManagementController {
    private final BrandService brandService;

    public BrandManagementController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public ResponseEntity<List<Brand>> getAllBrands() {
        return new ResponseEntity<>(brandService.getAllBrands(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Brand> createBrand(@RequestParam String name,
                                             @RequestParam String description,
                                             @RequestParam MultipartFile image) {
        return new ResponseEntity<>(brandService.createBrand(BrandRequest.builder().name(name).description(description).image(image).build()), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateBrand(@RequestParam int id,
                                         @RequestParam(required = false) String name,
                                         @RequestParam(required = false) String description,
                                         @RequestParam(required = false) MultipartFile image) {
        return new ResponseEntity<>(brandService.updateBrand(id, BrandRequest.builder().name(name).description(description).image(image).build()), HttpStatus.CREATED);
    }
}
