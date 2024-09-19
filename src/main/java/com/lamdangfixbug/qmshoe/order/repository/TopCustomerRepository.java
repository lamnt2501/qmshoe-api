package com.lamdangfixbug.qmshoe.order.repository;

import com.lamdangfixbug.qmshoe.order.entity.TopCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TopCustomerRepository extends JpaRepository<TopCustomer, Integer> {

    @Query("select tc from TopCustomer tc order by tc.spend desc limit 8")
    List<TopCustomer> getTopCustomer();

    Optional<TopCustomer> findTopCustomerByCustomer_Id(int id);
}
