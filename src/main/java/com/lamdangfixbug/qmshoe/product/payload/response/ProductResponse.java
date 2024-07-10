package com.lamdangfixbug.qmshoe.product.payload.response;

import com.lamdangfixbug.qmshoe.product.entity.Category;
import com.lamdangfixbug.qmshoe.product.entity.Product;
import com.lamdangfixbug.qmshoe.product.entity.ProductImage;
import com.lamdangfixbug.qmshoe.product.entity.ProductOption;
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
    private double price;
    private String slug;
    private String brand;
    private String[] categories;
    private LocalDateTime createdAt;
    private List<ProductOptionResponse> options;
    private List<ProductImageResponse> images;

    public static ProductResponse from(final Product product) {
        ProductResponseBuilder builder = ProductResponse.builder();
        builder.id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .slug(product.getSlug())
                .brand(product.getBrand().getName())
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
        return builder.build();
    }
}
