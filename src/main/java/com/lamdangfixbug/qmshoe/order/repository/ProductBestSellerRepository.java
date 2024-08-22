package com.lamdangfixbug.qmshoe.order.repository;

import com.lamdangfixbug.qmshoe.order.entity.ProductBestSeller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductBestSellerRepository extends JpaRepository<ProductBestSeller, String> {
    @Query("select pbs from ProductBestSeller  pbs order by pbs.sold desc limit 8")
    List<ProductBestSeller> getProductBestSeller();

}
