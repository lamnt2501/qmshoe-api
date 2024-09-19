package com.lamdangfixbug.qmshoe.product.service;

import com.lamdangfixbug.qmshoe.product.entity.Brand;
import com.lamdangfixbug.qmshoe.product.payload.request.BrandRequest;

import java.util.List;

public interface BrandService {
    Brand createBrand(BrandRequest brandRequest);
    List<Brand> getAllBrands();
    Brand getBrandById(int id);
    Brand updateBrand(int id, BrandRequest brandRequest);
}
