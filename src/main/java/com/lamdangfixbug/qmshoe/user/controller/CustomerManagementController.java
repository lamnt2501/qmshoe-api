package com.lamdangfixbug.qmshoe.user.controller;

import com.lamdangfixbug.qmshoe.user.entity.Customer;
import com.lamdangfixbug.qmshoe.user.payload.response.CustomerResponse;
import com.lamdangfixbug.qmshoe.user.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController("api/v1/management/users")
public class CustomerManagementController {
    private final CustomerService customerService;

    public CustomerManagementController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers(@RequestParam Map<String,Object> params) {
        return ResponseEntity.ok(customerService.getAllCustomer(params));
    }
}
