package com.lamdangfixbug.qmshoe.user.repository;

import com.lamdangfixbug.qmshoe.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Integer> {
}
