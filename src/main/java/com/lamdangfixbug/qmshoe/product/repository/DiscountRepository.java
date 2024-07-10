package com.lamdangfixbug.qmshoe.product.repository;

import com.lamdangfixbug.qmshoe.product.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    Optional<Discount> findByName(String name);
}
