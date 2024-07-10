package com.lamdangfixbug.qmshoe.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lamdangfixbug.qmshoe.product.entity.Category;
import com.lamdangfixbug.qmshoe.product.payload.request.CategoryRequest;
import com.lamdangfixbug.qmshoe.product.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return new ResponseEntity<>(categoryService.findAllCategories(), HttpStatus.OK);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Category> getCategory(@PathVariable final String slug) {
        return new ResponseEntity<>(categoryService.findCategoryBySlug(slug), HttpStatus.OK);
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
