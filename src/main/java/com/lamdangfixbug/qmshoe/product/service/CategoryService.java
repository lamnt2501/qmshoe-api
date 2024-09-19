package com.lamdangfixbug.qmshoe.product.service;

import com.lamdangfixbug.qmshoe.product.entity.Category;
import com.lamdangfixbug.qmshoe.product.payload.request.CategoryRequest;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryRequest categoryRequest);
    Category updateCategory(Category category);
    Category findCategoryById(int id);
    Category findCategoryBySlug(String slug);
    List<Category> findAllCategories();
}
