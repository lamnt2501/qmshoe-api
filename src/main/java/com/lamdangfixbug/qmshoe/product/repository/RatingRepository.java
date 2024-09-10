package com.lamdangfixbug.qmshoe.product.repository;

import com.lamdangfixbug.qmshoe.product.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    @Query(value = "select * from ratings where product_id = :productId",nativeQuery = true)
    Page<Rating> findAllByProductId(Integer productId, Pageable pageable);
    @Query(value = "select * from ratings where customer_id = :customerId",nativeQuery = true)
    Page<Rating> findAllByCustomer_Id(Integer customerId, Pageable pageable);
    Optional<Rating> findByCustomer_IdAndProductId(Integer customerId, Integer productId);
    long countByProductId(Integer productId);
    @Query(value = "select * from ratings",nativeQuery = true)
    List<Rating> getAllRatings(Pageable pageable);
}
