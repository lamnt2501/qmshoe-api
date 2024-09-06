package com.lamdangfixbug.qmshoe.user.repository;

import com.lamdangfixbug.qmshoe.user.entity.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByEmail(String email);
    @Query(value = "select * from customers",nativeQuery = true)
    List<Customer> getAllCustomer(Pageable pageable);
}
