package com.lamdangfixbug.qmshoe.product.service;

import com.lamdangfixbug.qmshoe.product.entity.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(Category category);
    Category updateCategory(Category category);
    Category findCategoryById(int id);
    Category findCategoryBySlug(String slug);
    List<Category> findAllCategories();
}
