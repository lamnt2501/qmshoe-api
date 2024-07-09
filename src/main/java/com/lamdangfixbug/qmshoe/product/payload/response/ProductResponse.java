package com.lamdangfixbug.qmshoe.product.payload.response;

import com.lamdangfixbug.qmshoe.product.entity.Category;
import com.lamdangfixbug.qmshoe.product.entity.Product;
import lombok.*;

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

    public static ProductResponse from(final Product product) {
        ProductResponseBuilder builder = ProductResponse.builder();
        builder.id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .slug(product.getSlug())
                .brand(product.getBrand().getName())
                .categories(
                        product.getCategories().stream().map(Category::getName).toArray(String[]::new)
                );

        return builder.build();
    }
}
