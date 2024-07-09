package com.lamdangfixbug.qmshoe.product.controller;

import com.lamdangfixbug.qmshoe.product.entity.Category;
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
    public ResponseEntity<Category> createCategory(@RequestParam String name, @RequestParam String description, @RequestParam(required = false) MultipartFile image) {
        Category category = categoryService
                .createCategory(
                        Category.builder()
                                .name(name)
                                .description(description)
                                .imgUrl(image == null ? "liam.jpeg" : image.getOriginalFilename())
                                .build());
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }


}
