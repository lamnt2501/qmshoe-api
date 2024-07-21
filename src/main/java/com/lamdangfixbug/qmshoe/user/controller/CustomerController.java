package com.lamdangfixbug.qmshoe.user.controller;

import com.lamdangfixbug.qmshoe.auth.payload.request.ChangePasswordRequest;
import com.lamdangfixbug.qmshoe.user.service.impl.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class CustomerController {
    private final UserDetailsServiceImpl userDetailsService;

    public CustomerController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        userDetailsService.changePassword(changePasswordRequest);
        return ResponseEntity.ok().build();
    }
}
