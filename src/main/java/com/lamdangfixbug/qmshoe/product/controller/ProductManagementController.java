package com.lamdangfixbug.qmshoe.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamdangfixbug.qmshoe.product.entity.Product;
import com.lamdangfixbug.qmshoe.product.payload.request.ProductOptionRequest;
import com.lamdangfixbug.qmshoe.product.payload.request.ProductRequest;
import com.lamdangfixbug.qmshoe.product.payload.response.ProductOptionResponse;
import com.lamdangfixbug.qmshoe.product.payload.response.ProductResponse;
import com.lamdangfixbug.qmshoe.product.service.ProductOptionService;
import com.lamdangfixbug.qmshoe.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/management/products")
public class ProductManagementController {
    private final ProductService productService;
    private final ProductOptionService productOptionService;

    public ProductManagementController(ProductService productService, ProductOptionService productOptionService) {
        this.productService = productService;
        this.productOptionService = productOptionService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        return new ResponseEntity<>(ProductResponse.from(productService.createProduct(productRequest)), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return new ResponseEntity<>(ProductResponse.from(productService.updateProduct(product)), HttpStatus.OK);
    }

    //--------------
    @PostMapping(value = "/variants")
    public ResponseEntity<ProductOptionResponse> createProductOption(@RequestParam("productOption") String productOptionRequestStr, @RequestParam MultipartFile[] images) {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductOptionRequest productOptionRequest = null;
        try {
            productOptionRequest = objectMapper.readValue(productOptionRequestStr, ProductOptionRequest.class);
            productOptionRequest.setImages(images);
        } catch (Exception e) {
            throw new RuntimeException("Could not parse product option request!");
        }

        return new ResponseEntity<>(ProductOptionResponse.from(productOptionService.createProductOption(productOptionRequest)), HttpStatus.CREATED);
    }

}
