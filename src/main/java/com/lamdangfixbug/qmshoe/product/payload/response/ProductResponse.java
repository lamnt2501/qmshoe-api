package com.lamdangfixbug.qmshoe.product.payload.response;

import com.lamdangfixbug.qmshoe.discount.payload.response.DiscountResponse;
import com.lamdangfixbug.qmshoe.product.entity.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductResponse {
    private int id;
    private String name;
    private String description;
    private String slug;
    private String brand;
    private DiscountResponse discount;
    private String[] categories;
    private List<ProductOptionResponse> options;
    private List<ProductImageResponse> images;
    private double avgRatings;
    private LocalDateTime createdAt;

    public static ProductResponse from(final Product product) {
        ProductResponseBuilder builder = ProductResponse.builder();
        builder.id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .slug(product.getSlug())
                .brand(product.getBrand().getName())
                .avgRatings(product.getAvgRatings())
                .createdAt(product.getCreatedAt())
                .categories(
                        product.getCategories().stream().map(Category::getName).toArray(String[]::new)
                );
        if (product.getProductOptions() != null) {
            builder.options(product.getProductOptions().stream().map(ProductOptionResponse::from).toList());
        }
        if (product.getProductImages() != null) {
            builder.images(product.getProductImages().stream().map(ProductImageResponse::from).toList());
        }
        if(product.getDiscount() != null) {
            builder.discount(DiscountResponse.from(product.getDiscount()));
        }
        return builder.build();
    }
}
