package com.lamdangfixbug.qmshoe.product.payload.response;

import com.lamdangfixbug.qmshoe.product.entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductImageResponse {
    private String url;
    private String color;

    public static ProductImageResponse from(final ProductImage productImage) {
        return ProductImageResponse.builder().url(productImage.getUrl()).color(productImage.getColor().getName()).build();
    }
}
