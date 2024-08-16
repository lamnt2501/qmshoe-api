package com.lamdangfixbug.qmshoe.discount.service.impl;

import com.lamdangfixbug.qmshoe.exceptions.ResourceNotFoundException;
import com.lamdangfixbug.qmshoe.discount.entity.Discount;
import com.lamdangfixbug.qmshoe.product.entity.Product;
import com.lamdangfixbug.qmshoe.discount.payload.request.DiscountRequest;
import com.lamdangfixbug.qmshoe.discount.repository.DiscountRepository;
import com.lamdangfixbug.qmshoe.product.repository.ProductRepository;
import com.lamdangfixbug.qmshoe.discount.service.DiscountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DiscountServiceImpl implements DiscountService {
    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;

    public DiscountServiceImpl(DiscountRepository discountRepository, ProductRepository productRepository) {
        this.discountRepository = discountRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Discount createDiscount(DiscountRequest discountRequest) {
        Discount discount = discountRepository.save(Discount.builder()
                .type(discountRequest.getType())
                .name(discountRequest.getName())
                .value(discountRequest.getValue())
                .maxUsage(discountRequest.getMaxUsage())
                .endAt(discountRequest.getEndAt())
                .startAt(discountRequest.getStartAt())
                .isOrderDiscount(discountRequest.isOrderDiscount())
                .build());
        List<Product> products = new ArrayList<>();
        for (int productId : discountRequest.getProductIds()) {
            Product p = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Could not find product with id " + productId));
            p.setDiscount(discount);
            productRepository.save(p);
            products.add(p);
        }
        discount.setProducts(products);
        discountRepository.save(discount);
        return discount;
    }

    @Override
    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    @Override
    public Discount getDiscountById(int id) {
        return discountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Couldn't find discount with id: " + id));
    }

    @Override
    @Transactional
    public Discount updateDiscount(DiscountRequest discountRequest) {
        Discount discount = discountRepository.findByName(discountRequest.getName()).orElseThrow(() -> new ResourceNotFoundException("Couldn't find discount with name: " + discountRequest.getName()));
        discount.setName(discountRequest.getName());
        discount.setValue(discountRequest.getValue());
        discount.setMaxUsage(discountRequest.getMaxUsage());
        discount.setEndAt(discountRequest.getEndAt());
        discount.setStartAt(discountRequest.getStartAt());
        List<Product> products = discount.getProducts();
        List<Product> updatedProducts = new ArrayList<>();
        Arrays.stream(discountRequest.getProductIds())
                .forEach(id -> updatedProducts.add(
                                productRepository.findById(id)
                                        .orElseThrow(() -> new ResourceNotFoundException("Couldn't find product with id:" + id)
                                        )
                        )
                );

        products.forEach(product -> {
            if (!updatedProducts.contains(product)) {
                product.setDiscount(null);
                productRepository.save(product);
            }
        });
        updatedProducts.forEach(product -> {
            if (!products.contains(product)) {
                product.setDiscount(discount);
                productRepository.save(product);
            }
        });
        discount.setProducts(updatedProducts);
        return discountRepository.save(discount);
    }

    @Override
    public List<Discount> getAllOrderDiscounts() {
        return discountRepository.findOrderDiscount();
    }
}
