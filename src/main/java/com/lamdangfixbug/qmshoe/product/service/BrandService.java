package com.lamdangfixbug.qmshoe.product.service;

import com.lamdangfixbug.qmshoe.product.entity.Brand;

import java.util.List;

public interface BrandService {
    Brand createBrand(Brand brand);
    List<Brand> getAllBrands();
    Brand getBrandById(int id);


}
