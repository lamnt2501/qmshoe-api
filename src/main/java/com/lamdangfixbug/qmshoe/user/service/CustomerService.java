package com.lamdangfixbug.qmshoe.user.service;

import com.lamdangfixbug.qmshoe.order.payload.response.TopCustomerResponse;
import com.lamdangfixbug.qmshoe.user.entity.Customer;
import com.lamdangfixbug.qmshoe.user.payload.response.CustomerResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CustomerService {
    List<CustomerResponse> getAllCustomer(Map<String, Object> params);
    CustomerResponse me();
    CustomerResponse getCustomer(int id);
    CustomerResponse getCustomer(String email);
    TopCustomerResponse myTopCustomer();
    TopCustomerResponse getTopCustomer(int id);
}
