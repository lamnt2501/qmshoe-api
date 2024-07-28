package com.lamdangfixbug.qmshoe.product.repository;

import com.lamdangfixbug.qmshoe.product.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SizeRepository extends JpaRepository<Size, Integer> {
    @Query("select s.size from Size s")
    List<String> getAllSizes();
    @Query("select s.id from Size  s")
    List<Integer> getAllIds();
    Optional<Size> findBySize(String size);
}
