package com.lamdangfixbug.qmshoe.user.service.impl;

import com.lamdangfixbug.qmshoe.exceptions.ResourceNotFoundException;
import com.lamdangfixbug.qmshoe.user.entity.Address;
import com.lamdangfixbug.qmshoe.user.entity.Customer;
import com.lamdangfixbug.qmshoe.user.repository.AddressRepository;
import com.lamdangfixbug.qmshoe.user.service.AddressService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address addAddress(Address address) {
        Customer c = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        address.setCustomerId(c.getId());
        return addressRepository.save(address);
    }

    @Override
    public Address getAddressById(int id) {
        Customer c = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return addressRepository.findByIdAndCustomerId(id, c.getId()).orElseThrow(() -> new ResourceNotFoundException("Address not found"));
    }

    @Override
    public List<Address> getAllAddress() {
        Customer c = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return addressRepository.findByCustomerId(c.getId());
    }

    @Override
    public Address updateAddress(int id, Address address) {
        Customer c = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Address addr = addressRepository.findByCustomerIdAndId(c.getId(), id).orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        String newCity = address.getCity();
        String newDistrict = address.getDistrict();
        String newWard = address.getWard();
        String newSpecificAddress = address.getSpecificAddress();

        if (newCity != null && !newCity.isEmpty()) addr.setCity(address.getCity());
        if (newDistrict != null && !newDistrict.isEmpty()) addr.setDistrict(address.getDistrict());
        if (newWard != null && !newWard.isEmpty()) addr.setWard(address.getWard());
        if (newSpecificAddress != null && !newSpecificAddress.isEmpty()) addr.setSpecificAddress(address.getSpecificAddress());
        return addressRepository.save(addr);
    }
}
