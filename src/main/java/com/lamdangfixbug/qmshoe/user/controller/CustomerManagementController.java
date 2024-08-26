package com.lamdangfixbug.qmshoe.user.controller;

import com.lamdangfixbug.qmshoe.user.entity.Customer;
import com.lamdangfixbug.qmshoe.user.payload.response.CustomerResponse;
import com.lamdangfixbug.qmshoe.user.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/management/users")
public class CustomerManagementController {
    private final CustomerService customerService;

    public CustomerManagementController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers(@RequestParam Map<String,Object> params) {
        return ResponseEntity.ok(customerService.getAllCustomer(params));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable int id) {
        return ResponseEntity.ok(customerService.getCustomer(id));
    }

    @PostMapping("/email")
    public ResponseEntity<CustomerResponse> getCustomerByEmail(@RequestParam String email) {
        return ResponseEntity.ok(customerService.getCustomer(email));
    }
}
