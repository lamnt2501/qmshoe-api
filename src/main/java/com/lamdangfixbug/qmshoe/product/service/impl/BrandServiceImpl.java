package com.lamdangfixbug.qmshoe.product.service.impl;

import com.lamdangfixbug.qmshoe.exceptions.ResourceNotFoundException;
import com.lamdangfixbug.qmshoe.product.entity.Brand;
import com.lamdangfixbug.qmshoe.product.payload.request.BrandRequest;
import com.lamdangfixbug.qmshoe.product.repository.BrandRepository;
import com.lamdangfixbug.qmshoe.product.service.BrandService;
import com.lamdangfixbug.qmshoe.product.service.FileUploadService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
private  final FileUploadService fileUploadService;
    public BrandServiceImpl(BrandRepository brandRepository, FileUploadService fileUploadService) {
        this.brandRepository = brandRepository;
        this.fileUploadService = fileUploadService;
    }

    @Override
    public Brand createBrand(BrandRequest brandRequest) {
        Brand brand = new Brand();
        brand.setName(brandRequest.getName());
        brand.setDescription(brandRequest.getDescription());
        brand.setImgUrl((String)fileUploadService.uploadImage(brandRequest.getImage()).get("url"));
        return brandRepository.save(brand);
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Brand getBrandById(int id) {
        return brandRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Couldn't find brand with id: " + id));
    }

    @Override
    public Brand updateBrand(int id, BrandRequest brandRequest) {
        Brand brand = getBrandById(id);
        String name = brandRequest.getName();
        if(name!= null && !name.isBlank())brand.setName(brandRequest.getName());
        String description = brandRequest.getDescription();
        if(description != null && !description.isBlank())brand.setDescription(brandRequest.getDescription());
        if(brandRequest.getImage()!=null)brand.setImgUrl((String)fileUploadService.uploadImage(brandRequest.getImage()).get("url"));
        return brandRepository.save(brand);
    }
}
