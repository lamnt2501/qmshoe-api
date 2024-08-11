package com.lamdangfixbug.qmshoe.user.repository;

import com.lamdangfixbug.qmshoe.user.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    Optional<Address> findByIdAndCustomerId(Integer id, Integer customerId);
    List<Address> findByCustomerId(Integer customerId);
    Optional<Address> findByCustomerIdAndId(Integer customerId, Integer addressId);
    Optional<Address> findByCustomerIdAndCityAndDistrictAndWardAndSpecificAddress(int customerId, String city, String district, String ward, String specificAddress);
}
