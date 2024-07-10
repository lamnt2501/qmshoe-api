package com.lamdangfixbug.qmshoe.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<List<ProductResponse>> getAllProducts(@RequestParam(value = "color",required = false) List<String> colors,
                                                                @RequestParam(value = "size",required = false) List<String> sizes,
                                                                @RequestParam(required = false) Map<String, Object> filters) {
        if(colors!=null){
            filters.put("colors", colors);
        }
        if(sizes!=null){
            filters.put("size", sizes);
        }
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
    @PostMapping(value = "/variants")
    public ResponseEntity<ProductOptionResponse> createProductOption(@RequestParam("productOption") String productOptionRequestStr, @RequestParam MultipartFile[] images) {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductOptionRequest productOptionRequest = null;
        try{
            productOptionRequest = objectMapper.readValue(productOptionRequestStr,ProductOptionRequest.class);
            productOptionRequest.setImages(images);
        }catch (Exception e){
            throw new RuntimeException("Could not parse product option request!");
        }

        return new ResponseEntity<>(ProductOptionResponse.from(productOptionService.createProductOption(productOptionRequest)),HttpStatus.CREATED);
    }

}
