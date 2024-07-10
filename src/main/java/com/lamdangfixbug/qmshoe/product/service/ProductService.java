package com.lamdangfixbug.qmshoe.product.service;

import com.lamdangfixbug.qmshoe.product.entity.Product;
import com.lamdangfixbug.qmshoe.product.payload.request.ProductRequest;

import java.util.List;
import java.util.Map;

public interface ProductService {
    Product createProduct(ProductRequest productRequest);
    Product updateProduct(Product product);
    Product findProductById(Integer id);
    Product findProductBySlug(String slug);
    List<Product> findAllProducts(Map<String, Object> params);
}
