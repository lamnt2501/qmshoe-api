package com.lamdangfixbug.qmshoe.order.repository;

import com.lamdangfixbug.qmshoe.order.entity.Order;
import com.lamdangfixbug.qmshoe.user.entity.Customer;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    @Query(value = "select o.* from orders o where o.customer_id =  :customerId",nativeQuery = true)
    Page<Order> findByCustomer(int customerId,Pageable pageable);

    @NotNull
    @Query(value = "select o.* from orders as o",nativeQuery = true)
    Page<Order> findAll(@NotNull Pageable pageable);

    Optional<Order> findByIdAndCustomer_Id(int id,int customerId);

}
