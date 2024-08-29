package com.lamdangfixbug.qmshoe.order.repository;

import com.lamdangfixbug.qmshoe.order.entity.Order;
import com.lamdangfixbug.qmshoe.order.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Integer> {
    @Query(value = "select o.* from orders o where o.customer_id =  :customerId",nativeQuery = true)
    Page<Order> findByCustomer(int customerId,Pageable pageable);


    @Query(value = "select id, created_at, phone_number, receiver_name, status, total, updated_at, address_id, customer_id, payment_details_id from orders as o",nativeQuery = true)
    Page<Order> findAllOrder( Pageable pageable);

    Optional<Order> findByIdAndCustomer_Id(int id,int customerId);

    int countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    int countByStatusAndCreatedAtBetween(OrderStatus status, LocalDateTime start, LocalDateTime end);

}
