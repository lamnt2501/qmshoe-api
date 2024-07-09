package com.lamdangfixbug.qmshoe.product.service.impl;

import com.lamdangfixbug.qmshoe.exceptions.ResourceNotFoundException;
import com.lamdangfixbug.qmshoe.product.entity.Brand;
import com.lamdangfixbug.qmshoe.product.entity.Category;
import com.lamdangfixbug.qmshoe.product.entity.Product;
import com.lamdangfixbug.qmshoe.product.payload.request.ProductRequest;
import com.lamdangfixbug.qmshoe.product.repository.BrandRepository;
import com.lamdangfixbug.qmshoe.product.repository.CategoryRepository;
import com.lamdangfixbug.qmshoe.product.repository.ProductRepository;
import com.lamdangfixbug.qmshoe.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, BrandRepository brandRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Product createProduct(ProductRequest productRequest) {
        Brand brand = brandRepository.findById(productRequest.getBrandId()).orElseThrow(() -> new ResourceNotFoundException("Couldn't find brand with id: " + productRequest.getBrandId()));
        List<Category> categories = new ArrayList<>();
        for (int id : productRequest.getCategoryId()) {
            categories.add(categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Couldn't find category with id: " + id)));
        }

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .brand(brand)
                .categories(categories)
                .build();
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        if(productRepository.existsById(product.getId())) {
            return productRepository.save(product);
        }
        throw new ResourceNotFoundException("Couldn't find product with id: " + product.getId());
    }

    @Override
    public Product findProductById(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Could not find product with id: " + id));
    }

    @Override
    public Product findProductBySlug(String slug) {
        return productRepository.findBySlug(slug).orElseThrow(() -> new ResourceNotFoundException("Could not find product with slug: " + slug));
    }

    @Override
    public List<Product> findAllProducts(Map<String, String> params) {
        return productRepository.findAll();
    }
}
