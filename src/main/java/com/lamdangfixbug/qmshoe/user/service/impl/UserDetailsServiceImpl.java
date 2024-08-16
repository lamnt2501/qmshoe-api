package com.lamdangfixbug.qmshoe.user.service.impl;

import com.lamdangfixbug.qmshoe.auth.payload.request.ChangePasswordRequest;
import com.lamdangfixbug.qmshoe.exceptions.PasswordDidNotMatchException;
import com.lamdangfixbug.qmshoe.user.entity.Customer;
import com.lamdangfixbug.qmshoe.user.entity.Staff;
import com.lamdangfixbug.qmshoe.user.payload.request.UpdateUserInformationRequest;
import com.lamdangfixbug.qmshoe.user.payload.response.CustomerResponse;
import com.lamdangfixbug.qmshoe.user.repository.CustomerRepository;
import com.lamdangfixbug.qmshoe.user.repository.StaffRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    public void changePassword(ChangePasswordRequest request) {
        UserDetails userDetails = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        String password = userDetails.getPassword();
        boolean isMatch = BCrypt.checkpw(request.getCurrentPassword().getBytes(), password);
        if (!isMatch) {
            throw new PasswordDidNotMatchException("Current password didn't match");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new PasswordDidNotMatchException("New password and confirm password didn't match");
        }
        String newPassword = new BCryptPasswordEncoder().encode(request.getNewPassword());
        if (userDetails.getAuthorities().isEmpty()) {
            Customer customer = (Customer) userDetails;
            customer.setPassword(newPassword);
            customerRepository.save(customer);
        }
        assert userDetails instanceof Staff;
        Staff staff = (Staff) userDetails;
        staff.setPassword(newPassword);
        staffRepository.save(staff);
    }

    public void updateUserInformation(UpdateUserInformationRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails.getAuthorities().isEmpty()) {
            Customer customer = (Customer) userDetails;
            if (request.getName() != null) customer.setName(request.getName());
            if (request.getPhoneNumber() != null) customer.setPhoneNumber(request.getPhoneNumber());
            customerRepository.save(customer);
        } else {
            Staff staff = (Staff) userDetails;
            if (request.getName() != null) staff.setName(request.getName());
            if (request.getPhoneNumber() != null) staff.setPhoneNumber(request.getPhoneNumber());
            staffRepository.save(staff);
        }
    }
}
