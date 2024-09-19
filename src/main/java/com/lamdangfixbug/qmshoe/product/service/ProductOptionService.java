package com.lamdangfixbug.qmshoe.product.service;

import com.lamdangfixbug.qmshoe.product.entity.ProductOption;
import com.lamdangfixbug.qmshoe.product.payload.request.ProductOptionRequest;

public interface ProductOptionService {
    ProductOption createProductOption(ProductOptionRequest productOptionRequest);
}
