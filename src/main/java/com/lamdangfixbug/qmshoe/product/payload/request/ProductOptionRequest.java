package com.lamdangfixbug.qmshoe.product.payload.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductOptionRequest {
    private int productId;
    private int colorId;
    private int sizeId;
    private int quantity;
    private double price;
    private MultipartFile[] images;
}
