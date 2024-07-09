package com.lamdangfixbug.qmshoe.product.service.impl;

import com.lamdangfixbug.qmshoe.exceptions.ResourceNotFoundException;
import com.lamdangfixbug.qmshoe.product.entity.Color;
import com.lamdangfixbug.qmshoe.product.entity.Product;
import com.lamdangfixbug.qmshoe.product.entity.ProductOption;
import com.lamdangfixbug.qmshoe.product.entity.Size;
import com.lamdangfixbug.qmshoe.product.payload.request.ProductOptionRequest;
import com.lamdangfixbug.qmshoe.product.repository.ColorRepository;
import com.lamdangfixbug.qmshoe.product.repository.ProductOptionRepository;
import com.lamdangfixbug.qmshoe.product.repository.ProductRepository;
import com.lamdangfixbug.qmshoe.product.repository.SizeRepository;
import com.lamdangfixbug.qmshoe.product.service.ProductOptionService;
import org.springframework.stereotype.Service;

@Service
public class ProductOptionServiceImpl implements ProductOptionService {
    private final ProductOptionRepository productOptionRepository;
    private final ProductRepository productRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;

    public ProductOptionServiceImpl(ProductOptionRepository productOptionRepository, ProductRepository productRepository, ColorRepository colorRepository, SizeRepository sizeRepository) {
        this.productOptionRepository = productOptionRepository;
        this.productRepository = productRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
    }

    @Override
    public ProductOption createProductOption(ProductOptionRequest productOptionRequest) {
        Product product = productRepository.findById(productOptionRequest.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Couldn't find product with id: " + productOptionRequest.getProductId()));
        Color color = colorRepository.findById(productOptionRequest.getColorId()).orElseThrow(() -> new ResourceNotFoundException("Couldn't find color with id: " + productOptionRequest.getColorId()));
        Size size = sizeRepository.findById(productOptionRequest.getSizeId()).orElseThrow(() -> new ResourceNotFoundException("Couldn't find size with id: " + productOptionRequest.getSizeId()));
        ProductOption productOption = ProductOption.builder()
                .product(product)
                .color(color)
                .size(size)
                .quantity(productOptionRequest.getQuantity())
                .build();
        return productOptionRepository.save(productOption);
    }
}
