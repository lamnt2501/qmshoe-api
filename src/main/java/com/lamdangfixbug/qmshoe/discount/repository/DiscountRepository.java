package com.lamdangfixbug.qmshoe.discount.repository;

import com.lamdangfixbug.qmshoe.discount.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    Optional<Discount> findByName(String name);
}
