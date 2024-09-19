package com.lamdangfixbug.qmshoe.user.service.impl;

import com.lamdangfixbug.qmshoe.user.entity.Staff;
import com.lamdangfixbug.qmshoe.user.repository.StaffRepository;
import com.lamdangfixbug.qmshoe.user.service.StaffService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class StaffDetailsService implements StaffService {
    private final StaffRepository staffRepository;
    public StaffDetailsService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }
}
