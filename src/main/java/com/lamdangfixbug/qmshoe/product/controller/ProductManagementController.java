package com.lamdangfixbug.qmshoe.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/management/products")
public class ProductManagementController {
    private final ProductService productService;
    private final ProductOptionService productOptionService;

    public ProductManagementController(ProductService productService, ProductOptionService productOptionService) {
        this.productService = productService;
        this.productOptionService = productOptionService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(@RequestParam Map<String,Object> params) {
        return ResponseEntity.ok(productService.findAllProducts(params).stream().map(ProductResponse::from).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable int id) {
        return ResponseEntity.ok(ProductResponse.from(productService.findProductById(id)));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        return new ResponseEntity<>(ProductResponse.from(productService.createProduct(productRequest)), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable int id, @RequestBody String req) throws JsonProcessingException {
        ProductRequest productRequest = new ObjectMapper().readValue(req,ProductRequest.class);
        return new ResponseEntity<>(ProductResponse.from(productService.updateProduct(id,productRequest)), HttpStatus.OK);
    }

    @PatchMapping("/a/{id}")
    public ResponseEntity<?> activeProduct(@PathVariable int id,@RequestParam boolean active) {
        productService.activeProduct(id,active);
        Map<String,String> res = new HashMap<>();
                res.put("message","update success!");
                res.put("statusCode","200");
        return ResponseEntity.ok(res);
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
