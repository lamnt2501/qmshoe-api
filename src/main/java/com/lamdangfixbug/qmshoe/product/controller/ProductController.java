package com.lamdangfixbug.qmshoe.product.controller;

import com.lamdangfixbug.qmshoe.product.entity.Product;
import com.lamdangfixbug.qmshoe.product.entity.ProductOption;
import com.lamdangfixbug.qmshoe.product.payload.request.ProductOptionRequest;
import com.lamdangfixbug.qmshoe.product.payload.request.ProductRequest;
import com.lamdangfixbug.qmshoe.product.payload.response.ProductOptionResponse;
import com.lamdangfixbug.qmshoe.product.payload.response.ProductResponse;
import com.lamdangfixbug.qmshoe.product.service.ProductOptionService;
import com.lamdangfixbug.qmshoe.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;
    private final ProductOptionService productOptionService;
    public ProductController(ProductService productService, ProductOptionService productOptionService) {
        this.productService = productService;
        this.productOptionService = productOptionService;
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String slug) {
        return new ResponseEntity<>(ProductResponse.from(productService.findProductBySlug(slug)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(@RequestParam(required = false) Map<String, String> filters) {
        return new ResponseEntity<>(productService.findAllProducts(filters).stream().map(ProductResponse::from).toList(),HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        return new ResponseEntity<>(ProductResponse.from(productService.createProduct(productRequest)),HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return new ResponseEntity<>(ProductResponse.from(productService.updateProduct(product)), HttpStatus.OK);
    }

    //--------------
    @PostMapping("/variants")
    public ResponseEntity<ProductOptionResponse> createProductOption(@RequestBody ProductOptionRequest productOptionRequest) {
        return new ResponseEntity<>(ProductOptionResponse.from(productOptionService.createProductOption(productOptionRequest)),HttpStatus.CREATED);
    }

}
