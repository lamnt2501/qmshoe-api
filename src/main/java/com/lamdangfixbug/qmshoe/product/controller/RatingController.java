package com.lamdangfixbug.qmshoe.product.controller;

import com.lamdangfixbug.qmshoe.product.payload.request.RatingRequest;
import com.lamdangfixbug.qmshoe.product.payload.response.RatingResponse;
import com.lamdangfixbug.qmshoe.product.service.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/ratings")
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public ResponseEntity<RatingResponse> rate(@RequestBody RatingRequest ratingRequest) {
        return new ResponseEntity<>(ratingService.rate(ratingRequest), HttpStatus.CREATED);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<List<RatingResponse>> getAllProductRatings(@PathVariable int id, @RequestParam Map<String, Object> params) {
        return new ResponseEntity<>(ratingService.getRatingsByProductId(id, params), HttpStatus.OK);
    }
}
