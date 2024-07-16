package com.lamdangfixbug.qmshoe.user.service.impl;

import com.lamdangfixbug.qmshoe.user.repository.CustomerRepository;
import com.lamdangfixbug.qmshoe.user.repository.StaffRepository;
import com.lamdangfixbug.qmshoe.user.service.CustomerService;
import com.lamdangfixbug.qmshoe.user.service.StaffService;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;

    public UserDetailsServiceImpl(CustomerRepository customerRepository, StaffRepository staffRepository) {
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails customer = customerRepository.findByEmail(username).orElse(null);
        if (customer != null) {
            return customer;
        }
        UserDetails staff = staffRepository.findByEmail(username).orElse(null);
        if (staff != null) {
            return staff;
        }
        throw new UsernameNotFoundException(username);
    }
}
