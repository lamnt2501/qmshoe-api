package com.lamdangfixbug.qmshoe.product.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamdangfixbug.qmshoe.exceptions.ResourceNotFoundException;
import com.lamdangfixbug.qmshoe.product.entity.Brand;
import com.lamdangfixbug.qmshoe.product.entity.Category;
import com.lamdangfixbug.qmshoe.product.entity.Product;
import com.lamdangfixbug.qmshoe.product.payload.request.ProductRequest;
import com.lamdangfixbug.qmshoe.product.repository.*;
import com.lamdangfixbug.qmshoe.product.service.ProductService;
import com.lamdangfixbug.qmshoe.utils.Utils;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;
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
                .brand(brand)
                .categories(categories)
                .build();
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(int id, ProductRequest productRequest) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product with id: " + id + " not found"));
        String name = productRequest.getName();
        if (name != null && !name.isBlank()) {
            product.setName(name);
            product.setSlug(Utils.getSlug(name));
        }
        String des = productRequest.getDescription();
        if (des != null && !des.isBlank()) {
            product.setDescription(des);
        }
        int brandId = productRequest.getBrandId();
        if (brandId != 0) {
            if (brandId != product.getBrand().getId()) {
                product.setBrand(brandRepository.findById(brandId).orElseThrow(() -> new ResourceNotFoundException("Couldn't find brand with id: " + brandId)));
            }
        }
        int[] categoryId = productRequest.getCategoryId();
        if (categoryId != null && categoryId.length > 0) {
            List<Category> categories = new ArrayList<>();
            for (int cid : categoryId) {
                categories.add(categoryRepository.findById(cid).orElseThrow(() -> new ResourceNotFoundException("Couldn't find category with id: " + cid)));
            }
            product.setCategories(categories);
        }
        return productRepository.save(product);
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
        List<Integer> active = new ArrayList<>();
        active.add(1);
        var p = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (p != "anonymousUser" && !((UserDetails)p).getAuthorities().isEmpty()) {
            active.add(0);
        }

        Pageable pageable = Utils.buildPageable(params);
        if (params.isEmpty()) return productRepository.getAllProduct(active,pageable);
        List<Integer> colors = params.get("colors") != null ? List.class.cast(params.get("colors")) : colorRepository.getAllIds();
        List<Integer> sizes = params.get("sizes") != null ? List.class.cast(params.get("sizes")) : sizeRepository.getAllIds();
        int categoryId = params.get("category") != null ? Integer.parseInt((String) params.get("category")) : 1;
        double minPrice = params.get("minPrice") != null ? Double.parseDouble((String) params.get("minPrice")) : 0;
        double maxPrice = params.get("maxPrice") != null ? Double.parseDouble((String) params.get("maxPrice")) : 3000000;
        String name = params.get("name") != null ? params.get("name").toString() : "";
        return productRepository
                .getFilteredProduct(categoryId, minPrice, maxPrice, colors, sizes,name,
//                        active,
                        pageable);
    }

    @Override
    public void activeProduct(int id, boolean active) {
        Product product = findProductById(id);
        product.setActive(active);
        productRepository.save(product);
    }

}
