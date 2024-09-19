package com.lamdangfixbug.qmshoe.discount.repository;

import com.lamdangfixbug.qmshoe.discount.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    Optional<Discount> findByName(String name);
    @Query("select d from Discount  d where d.isOrderDiscount = true")
    List<Discount> findOrderDiscount();
}
