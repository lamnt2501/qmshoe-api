package com.lamdangfixbug.qmshoe.product.repository;

import com.lamdangfixbug.qmshoe.product.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SizeRepository extends JpaRepository<Size, Integer> {
    @Query("select s.size from Size s")
    List<String> getAllSizes();
    @Query("select s.id from Size  s")
    List<Integer> getAllIds();
}
