package com.lamdangfixbug.qmshoe.user.service.impl;

import com.lamdangfixbug.qmshoe.exceptions.ResourceNotFoundException;
import com.lamdangfixbug.qmshoe.order.payload.response.TopCustomerResponse;
import com.lamdangfixbug.qmshoe.order.repository.TopCustomerRepository;
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
    private final TopCustomerRepository topCustomerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, TopCustomerRepository topCustomerRepository) {
        this.customerRepository = customerRepository;
        this.topCustomerRepository = topCustomerRepository;
    }

    @Override
    public List<CustomerResponse> getAllCustomer(Map<String, Object> params) {
        return customerRepository.getAllCustomer(Utils.buildPageable(params)).stream().map(CustomerResponse::from).toList();
    }

    public CustomerResponse me() {
        UserDetails userDetails = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Customer customer = (Customer) userDetails;
        return CustomerResponse.from(customer);
    }

    @Override
    public CustomerResponse getCustomer(int id) {
        return CustomerResponse.from(customerRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Customer with id " + id + " not found")));
    }

    @Override
    public CustomerResponse getCustomer(String email) {

        return CustomerResponse.from(customerRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("Customer with email " + email + " not found")));
    }

    @Override
    public TopCustomerResponse myTopCustomer() {
        Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return TopCustomerResponse.from(topCustomerRepository.findTopCustomerByCustomer_Id(customer.getId()).orElseThrow(()->new ResourceNotFoundException("Top customer with id " + customer.getId() + " not found")));
    }

    @Override
    public TopCustomerResponse getTopCustomer(int id) {
        return TopCustomerResponse.from(topCustomerRepository.findTopCustomerByCustomer_Id(id).orElseThrow(()->new ResourceNotFoundException("Top customer with id " + id + " not found")));
    }
}
