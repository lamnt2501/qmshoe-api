package com.lamdangfixbug.qmshoe.product.service;

import com.lamdangfixbug.qmshoe.product.payload.request.RatingRequest;
import com.lamdangfixbug.qmshoe.product.payload.response.RatingResponse;

import java.util.List;
import java.util.Map;

public interface RatingService {
    RatingResponse rate(RatingRequest rating);
    RatingResponse getRatingById(int id);
    List<RatingResponse> getAllRatings(Map<String,Object> params);
    List<RatingResponse> getRatingsByProductId(int productId,Map<String,Object> params);
    List<RatingResponse> getRatingsByUserId(int userId, Map<String,Object> params);
    void deleteRatingById(int id);
}
