package com.lamdangfixbug.qmshoe.product.service.impl;

import com.lamdangfixbug.qmshoe.exceptions.ResourceNotFoundException;
import com.lamdangfixbug.qmshoe.product.entity.*;
import com.lamdangfixbug.qmshoe.product.payload.request.ProductOptionRequest;
import com.lamdangfixbug.qmshoe.product.repository.*;
import com.lamdangfixbug.qmshoe.product.service.FileUploadService;
import com.lamdangfixbug.qmshoe.product.service.ProductOptionService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class ProductOptionServiceImpl implements ProductOptionService {
    private final ProductOptionRepository productOptionRepository;
    private final ProductRepository productRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final ProductImageRepository productImageRepository;
    private final FileUploadService fileUploadService;

    public ProductOptionServiceImpl(ProductOptionRepository productOptionRepository,
                                    ProductRepository productRepository,
                                    ColorRepository colorRepository,
                                    SizeRepository sizeRepository,
                                    ProductImageRepository productImageRepository,
                                    FileUploadService fileUploadService) {
        this.productOptionRepository = productOptionRepository;
        this.productRepository = productRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.productImageRepository = productImageRepository;
        this.fileUploadService = fileUploadService;
    }

    @Override
    public ProductOption createProductOption(ProductOptionRequest productOptionRequest) {
        Product product = productRepository.findById(productOptionRequest.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Couldn't find product with id: " + productOptionRequest.getProductId()));
        Color color = colorRepository.findById(productOptionRequest.getColorId()).orElseThrow(() -> new ResourceNotFoundException("Couldn't find color with id: " + productOptionRequest.getColorId()));
        Size size = sizeRepository.findById(productOptionRequest.getSizeId()).orElseThrow(() -> new ResourceNotFoundException("Couldn't find size with id: " + productOptionRequest.getSizeId()));

        for (MultipartFile image : productOptionRequest.getImages()) {
            Map uploadResponse = fileUploadService.uploadImage(image);
            productImageRepository.save(
                    ProductImage.builder()
                            .product(product)
                            .color(color)
                            .url(uploadResponse.get("url").toString())
                            .build());
        }

        ProductOption productOption = ProductOption.builder()
                .product(product)
                .color(color)
                .size(size)
                .quantity(productOptionRequest.getQuantity())
                .build();
        return productOptionRepository.save(productOption);
    }
}
