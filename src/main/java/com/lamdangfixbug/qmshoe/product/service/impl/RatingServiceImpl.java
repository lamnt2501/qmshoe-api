package com.lamdangfixbug.qmshoe.product.service.impl;

import com.lamdangfixbug.qmshoe.product.entity.Product;
import com.lamdangfixbug.qmshoe.product.entity.Rating;
import com.lamdangfixbug.qmshoe.product.payload.request.RatingRequest;
import com.lamdangfixbug.qmshoe.product.payload.response.RatingResponse;
import com.lamdangfixbug.qmshoe.product.repository.ProductRepository;
import com.lamdangfixbug.qmshoe.product.repository.RatingRepository;
import com.lamdangfixbug.qmshoe.product.service.RatingService;
import com.lamdangfixbug.qmshoe.user.entity.Customer;
import com.lamdangfixbug.qmshoe.utils.Utils;
import jdk.jshell.execution.Util;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final ProductRepository productRepository;

    public RatingServiceImpl(RatingRepository ratingRepository, ProductRepository productRepository) {
        this.ratingRepository = ratingRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void deleteRatingById(int id) {

    }

    @Override
    public List<RatingResponse> getAllRatings(Map<String, Object> params) {

        return ratingRepository.findAll(Utils.buildPageable(params)).stream().map(RatingResponse::from).toList();
    }

    @Override
    public RatingResponse getRatingById(int id) {
        return null;
    }

    @Override
    public List<RatingResponse> getRatingsByProductId(int productId, Map<String, Object> params) {
        return ratingRepository.findAllByProductId(productId, Utils.buildPageable(params)).map(RatingResponse::from).stream().toList();
    }

    @Override
    public List<RatingResponse> getRatingsByUserId(int userId, Map<String, Object> params) {
        return ratingRepository.findAllByCustomer_Id(userId, Utils.buildPageable(params)).map(RatingResponse::from).stream().toList();
    }

    @Override
    @Transactional
    public RatingResponse rate(RatingRequest rating) {
        Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Product product = productRepository.findById(rating.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));

        long ratingCount = ratingRepository.countByProductId(rating.getProductId());
        double currentTotalRatings = product.getAvgRatings()*ratingCount;
        double newTotalRatings = currentTotalRatings + rating.getRating();

        Rating r = ratingRepository.findByCustomer_IdAndProductId(customer.getId(),rating.getProductId()).orElse(null);
        if(r != null) {
            product.setAvgRatings((newTotalRatings-r.getRatingValue())/ratingCount);
            r.setRatingValue(rating.getRating());
            r.setComment(rating.getComment());
            r = ratingRepository.save(r);
        }else{
            r = ratingRepository.save(
                    Rating.builder().customer(customer).productId(rating.getProductId()).comment(rating.getComment()).ratingValue(rating.getRating()).build()
            );
            product.setAvgRatings(newTotalRatings / (ratingCount + 1));
        }

        productRepository.save(product);
        return RatingResponse.from(r);
    }
}
