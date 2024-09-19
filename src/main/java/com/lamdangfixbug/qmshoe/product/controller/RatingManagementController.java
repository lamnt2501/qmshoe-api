package com.lamdangfixbug.qmshoe.product.controller;

import com.lamdangfixbug.qmshoe.product.entity.Rating;
import com.lamdangfixbug.qmshoe.product.payload.response.RatingResponse;
import com.lamdangfixbug.qmshoe.product.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/management/ratings")
public class RatingManagementController {
    private final RatingService ratingService;

    public RatingManagementController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping
    public ResponseEntity<List<RatingResponse>> getRatings(@RequestParam Map<String, Object> params) {
        return ResponseEntity.ok(ratingService.getAllRatings(params));
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<List<RatingResponse>> getRatings(@PathVariable int id, @RequestParam Map<String, Object> params) {
        return ResponseEntity.ok(ratingService.getRatingsByProductId(id, params));
    }
}
