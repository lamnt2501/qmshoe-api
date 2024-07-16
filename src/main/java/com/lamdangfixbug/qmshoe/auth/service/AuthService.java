package com.lamdangfixbug.qmshoe.auth.service;

import com.lamdangfixbug.qmshoe.auth.payload.request.LoginRequest;
import com.lamdangfixbug.qmshoe.auth.payload.request.RegisterRequest;
import com.lamdangfixbug.qmshoe.auth.payload.response.AuthenticationResponse;
import com.lamdangfixbug.qmshoe.exceptions.EmailAlreadyExistException;
import com.lamdangfixbug.qmshoe.user.entity.Customer;
import com.lamdangfixbug.qmshoe.user.entity.Role;
import com.lamdangfixbug.qmshoe.user.repository.CustomerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthenticationResponse register(RegisterRequest req) {
        if (customerRepository.findByEmail(req.getEmail()).orElse(null) != null) {
            throw new EmailAlreadyExistException();
        }

        Customer customer = Customer.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(Role.USER)
                .build();
        customerRepository.save(customer);
        String token = jwtService.generateToken(customer);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(token);
        return response;
    }

    public AuthenticationResponse login(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(), req.getPassword()
                )
        );

        String token = jwtService.generateToken(customerRepository.findByEmail(req.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Email not found")));
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(token);
        return response;
    }
}
