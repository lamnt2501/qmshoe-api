package com.lamdangfixbug.qmshoe.product.repository;

import com.lamdangfixbug.qmshoe.product.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ColorRepository extends JpaRepository<Color, Integer> {
    @Query("select c.slug from Color c")
    List<String> getAllSlugs();
    @Query("select c.id from Color c")
    List<Integer> getAllIds();
}
