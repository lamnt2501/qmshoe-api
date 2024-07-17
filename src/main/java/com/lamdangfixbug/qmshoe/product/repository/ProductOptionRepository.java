package com.lamdangfixbug.qmshoe.product.repository;

import com.lamdangfixbug.qmshoe.product.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductOptionRepository extends JpaRepository<ProductOption,Integer> {
    Optional<ProductOption> findBySku(String sku);
}
