package com.lamdangfixbug.qmshoe.user.service;

import com.lamdangfixbug.qmshoe.user.entity.Address;

import java.util.List;

public interface AddressService {
    List<Address> getAllAddress();
    Address updateAddress(int id,Address address);
    Address addAddress(Address address);
    Address getAddressById(int id);
}
