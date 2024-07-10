package com.lamdangfixbug.qmshoe.product.service.impl;

import com.lamdangfixbug.qmshoe.exceptions.ResourceNotFoundException;
import com.lamdangfixbug.qmshoe.product.entity.Category;
import com.lamdangfixbug.qmshoe.product.payload.request.CategoryRequest;
import com.lamdangfixbug.qmshoe.product.repository.CategoryRepository;
import com.lamdangfixbug.qmshoe.product.service.CategoryService;
import com.lamdangfixbug.qmshoe.product.service.FileUploadService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final FileUploadService fileUploadService;
    public CategoryServiceImpl(CategoryRepository categoryRepository, FileUploadService fileUploadService) {
        this.categoryRepository = categoryRepository;
        this.fileUploadService = fileUploadService;
    }

    @Override
    public Category createCategory(CategoryRequest categoryRequest) {
        Category category = Category.builder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .imgUrl(fileUploadService.uploadImage(categoryRequest.getImage()).get("url").toString())
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findCategoryById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category findCategoryBySlug(String slug) {
        return categoryRepository.findBySlug(slug);
    }

    @Override
    public Category updateCategory(Category category) {
        if (categoryRepository.existsById(category.getId())) {
            return categoryRepository.save(category);
        }
        throw  new ResourceNotFoundException("Couldn't find category with id: " + category.getId());
    }
}
