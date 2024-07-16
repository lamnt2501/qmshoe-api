package com.lamdangfixbug.qmshoe.product.service.impl;

import com.lamdangfixbug.qmshoe.exceptions.ResourceNotFoundException;
import com.lamdangfixbug.qmshoe.product.entity.Brand;
import com.lamdangfixbug.qmshoe.product.entity.Category;
import com.lamdangfixbug.qmshoe.product.entity.Product;
import com.lamdangfixbug.qmshoe.product.payload.request.ProductRequest;
import com.lamdangfixbug.qmshoe.product.repository.*;
import com.lamdangfixbug.qmshoe.product.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;

    public ProductServiceImpl(ProductRepository productRepository, BrandRepository brandRepository, CategoryRepository categoryRepository, ColorRepository colorRepository, SizeRepository sizeRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
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
        if (productRepository.existsById(product.getId())) {
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
    public List<Product> findAllProducts(Map<String, Object> params) {
        Pageable pageable = buildPageable(params);
        if(params.isEmpty()) return productRepository.getAllProduct(pageable);
        List<Integer> colors = params.get("colors") != null ? List.class.cast(params.get("colors")) : colorRepository.getAllIds();
        List<Integer> sizes = params.get("sizes") != null ? List.class.cast(params.get("sizes")) : sizeRepository.getAllIds();
        int categoryId = params.get("category") != null ? Integer.parseInt((String) params.get("category")) : 1;
        double minPrice = params.get("minPrice") != null ? Double.parseDouble((String) params.get("minPrice")) : 0;
        double maxPrice = params.get("maxPrice") != null ? Double.parseDouble((String) params.get("maxPrice")) : 200000;
        return productRepository
                .getFilteredProduct(categoryId,minPrice, maxPrice, colors, sizes, pageable);
    }


    private static Pageable buildPageable(Map<String, Object> params) {
        int page = 0;
        int limit = 20;
        String sortBy = "created_At";
        Sort.Direction order = Sort.Direction.DESC;
        if (params.containsKey("page")) {
            page = Math.max(Integer.parseInt((String) params.get("page")) - 1, 0);
        }
        if (params.containsKey("limit")) {
            limit = Math.max(Integer.parseInt((String) params.get("limit")), 1);
        }
        if (params.containsKey("sort")) {
            sortBy = (String) params.get("sort");
            order = Sort.Direction.ASC;
        }
        if (params.containsKey("order")) {
            order = Sort.Direction.valueOf((String) params.get("order"));
        }

        return PageRequest.of(page, limit, Sort.by(order, sortBy));
    }
}
