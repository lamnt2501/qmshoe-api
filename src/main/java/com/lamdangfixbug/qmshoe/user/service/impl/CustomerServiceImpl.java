package com.lamdangfixbug.qmshoe.user.service.impl;

import com.lamdangfixbug.qmshoe.user.entity.Customer;
import com.lamdangfixbug.qmshoe.user.payload.response.CustomerResponse;
import com.lamdangfixbug.qmshoe.user.repository.CustomerRepository;
import com.lamdangfixbug.qmshoe.user.service.CustomerService;
import com.lamdangfixbug.qmshoe.utils.Utils;
import jdk.jshell.execution.Util;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerResponse> getAllCustomer(Map<String, Object> params) {
        return customerRepository.findAll(Utils.buildPageable(params)).map(CustomerResponse::from).getContent();
    }

    public CustomerResponse me() {
        UserDetails userDetails = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Customer customer = (Customer) userDetails;
        return CustomerResponse.from(customer);
    }
}
