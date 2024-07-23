package com.lamdangfixbug.qmshoe.product.controller;

import com.lamdangfixbug.qmshoe.product.entity.Category;
import com.lamdangfixbug.qmshoe.product.payload.request.CategoryRequest;
import com.lamdangfixbug.qmshoe.product.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/management/categories")
public class CategoryManagementController {
    public CategoryService categoryService;

    public CategoryManagementController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestParam String name,
                                                   @RequestParam String description,
                                                   @RequestParam MultipartFile image) {
        Category category = categoryService.createCategory(
                CategoryRequest.builder().name(name).description(description).image(image).build()
        );
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }
}
