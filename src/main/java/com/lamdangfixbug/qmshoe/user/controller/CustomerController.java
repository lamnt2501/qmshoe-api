package com.lamdangfixbug.qmshoe.user.controller;

import com.lamdangfixbug.qmshoe.auth.payload.request.ChangePasswordRequest;
import com.lamdangfixbug.qmshoe.user.entity.Address;
import com.lamdangfixbug.qmshoe.user.payload.request.UpdateUserInformationRequest;
import com.lamdangfixbug.qmshoe.user.payload.response.CustomerResponse;
import com.lamdangfixbug.qmshoe.user.service.AddressService;
import com.lamdangfixbug.qmshoe.user.service.CustomerService;
import com.lamdangfixbug.qmshoe.user.service.impl.UserDetailsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class CustomerController {
    private final UserDetailsServiceImpl userDetailsService;
    private final CustomerService customerService;
    private final AddressService addressService;

    public CustomerController(UserDetailsServiceImpl userDetailsService, CustomerService customerService, AddressService addressService) {
        this.userDetailsService = userDetailsService;
        this.customerService = customerService;
        this.addressService = addressService;
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        userDetailsService.changePassword(changePasswordRequest);
        Map<String, String> res = new HashMap<>();
        res.put("Message", "Password changed successfully");
        res.put("StatusCode", "200");
        return ResponseEntity.ok(res);
    }

    @PutMapping
    public ResponseEntity<?> updateInformation(@RequestBody UpdateUserInformationRequest request) {
        userDetailsService.updateUserInformation(request);
        Map<String, String> res = new HashMap<>();
        res.put("Message", "Update successfully!");
        res.put("StatusCode", "200");
        return ResponseEntity.ok(res);
    }

    @PutMapping("/avatar")
    public ResponseEntity<?> updateAvatar(@RequestParam MultipartFile avatar) {
        userDetailsService.changeAvatar(avatar);
        Map<String, String> res = new HashMap<>();
        res.put("Message", "Update successfully!");
        res.put("StatusCode", "200");
        return ResponseEntity.ok(res);
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        return ResponseEntity.ok(userDetailsService.me());
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<Address>> getAddresses() {
        return ResponseEntity.ok(addressService.getAllAddress());
    }
}
