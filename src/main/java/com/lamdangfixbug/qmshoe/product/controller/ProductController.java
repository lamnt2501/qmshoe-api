package com.lamdangfixbug.qmshoe.product.controller;

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

    public ProductController(ProductService productService, ProductOptionService productOptionService) {
        this.productService = productService;
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable String slug) {
        return new ResponseEntity<>(ProductResponse.from(productService.findProductBySlug(slug)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts(@RequestParam(value = "color", required = false) List<String> colors,
                                                                @RequestParam(value = "size", required = false) List<String> sizes,
                                                                @RequestParam(required = false) Map<String, Object> filters) {
        if (colors != null) {
            filters.put("colors", colors);
        }
        if (sizes != null) {
            filters.put("sizes", sizes);
        }
        return new ResponseEntity<>(productService.findAllProducts(filters).stream().map(ProductResponse::from).toList(), HttpStatus.OK);
    }


}
