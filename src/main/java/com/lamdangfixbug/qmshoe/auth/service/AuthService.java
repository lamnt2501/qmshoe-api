package com.lamdangfixbug.qmshoe.auth.service;

import com.lamdangfixbug.qmshoe.auth.payload.request.LoginRequest;
import com.lamdangfixbug.qmshoe.auth.payload.request.RegisterRequest;
import com.lamdangfixbug.qmshoe.auth.payload.response.AuthenticationResponse;
import com.lamdangfixbug.qmshoe.cart.entity.Cart;
import com.lamdangfixbug.qmshoe.cart.repository.CartRepository;
import com.lamdangfixbug.qmshoe.exceptions.EmailAlreadyExistException;
import com.lamdangfixbug.qmshoe.user.entity.Customer;
import com.lamdangfixbug.qmshoe.user.entity.Staff;
import com.lamdangfixbug.qmshoe.user.entity.Token;
import com.lamdangfixbug.qmshoe.user.repository.CustomerRepository;
import com.lamdangfixbug.qmshoe.user.repository.TokenRepository;
import com.lamdangfixbug.qmshoe.utils.EmailService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final CartRepository cartRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    public AuthService(CustomerRepository customerRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       UserDetailsService userDetailsService, CartRepository cartRepository, TokenRepository tokenRepository, EmailService emailService) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.cartRepository = cartRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }

    public AuthenticationResponse register(RegisterRequest req) {
        if (customerRepository.findByEmail(req.getEmail()).orElse(null) != null) {
            throw new EmailAlreadyExistException();
        }

        Customer customer = Customer.builder()
                .name(req.getName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .build();

        customerRepository.save(customer);
        cartRepository.save(Cart.builder().customerId(customer.getId()).build());
        String token = jwtService.generateToken(customer);
        saveToken(customer,token);
//        String[] customerName = customer.getName().split(" ");
//        emailService.sendWelcomeEmail(customer.getEmail(),customerName[customerName.length-1]);

        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(token);
        return response;
    }

    public AuthenticationResponse login(LoginRequest req) {
        var a = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(), req.getPassword()
                )
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(req.getEmail());
        String token = jwtService.generateToken(userDetails);
        revokeAllToken(userDetails);
        saveToken(userDetails, token);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(token);
        return response;
    }

    public void saveToken(UserDetails userDetails, String token) {
        Token t = Token.builder()
                .token(token)
                .isRevoke(false)
                .build();
        if (userDetails instanceof Customer) {
            t.setBelongToUser("c" + ((Customer) userDetails).getId());
        }else{
            t.setBelongToUser("s" + ((Staff)userDetails).getId());
        }
        tokenRepository.save(t);
    }

    public void revokeAllToken(UserDetails userDetails) {
        String belongToUser = "";
        if(userDetails instanceof Customer){
            belongToUser = "c" + ((Customer) userDetails).getId();
        }else{
            belongToUser = "s" + ((Staff)userDetails).getId();
        }
        for(Token token : tokenRepository.findAllValidTokenByUser(belongToUser)){
            token.setRevoke(true);
            tokenRepository.save(token);
        };
    }
}
