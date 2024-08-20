package com.lamdangfixbug.qmshoe.auth.service;

import com.lamdangfixbug.qmshoe.auth.payload.request.LoginRequest;
import com.lamdangfixbug.qmshoe.auth.payload.request.RegisterRequest;
import com.lamdangfixbug.qmshoe.auth.payload.response.AuthenticationResponse;
import com.lamdangfixbug.qmshoe.cart.entity.Cart;
import com.lamdangfixbug.qmshoe.cart.repository.CartRepository;
import com.lamdangfixbug.qmshoe.exceptions.EmailAlreadyExistException;
import com.lamdangfixbug.qmshoe.exceptions.InvalidTokenException;
import com.lamdangfixbug.qmshoe.user.entity.Customer;
import com.lamdangfixbug.qmshoe.user.entity.Staff;
import com.lamdangfixbug.qmshoe.user.entity.Token;
import com.lamdangfixbug.qmshoe.user.entity.TokenType;
import com.lamdangfixbug.qmshoe.user.repository.CustomerRepository;
import com.lamdangfixbug.qmshoe.user.repository.StaffRepository;
import com.lamdangfixbug.qmshoe.user.repository.TokenRepository;
import com.lamdangfixbug.qmshoe.utils.EmailService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

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
    private final StaffRepository staffRepository;
    @Value("${qm.reset-password-url}")
    private String resetPasswordUrl;

    public AuthService(CustomerRepository customerRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       UserDetailsService userDetailsService, CartRepository cartRepository, TokenRepository tokenRepository, EmailService emailService, StaffRepository staffRepository) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.cartRepository = cartRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.staffRepository = staffRepository;
    }

    public AuthenticationResponse register(RegisterRequest req) {
        if (customerRepository.findByEmail(req.getEmail()).orElse(null) != null) {
            throw new EmailAlreadyExistException();
        }

        Customer customer = Customer.builder()
                .name(req.getName())
                .email(req.getEmail())
                .phoneNumber(req.getPhoneNumber())
                .password(passwordEncoder.encode(req.getPassword()))
                .build();

        customerRepository.save(customer);
        cartRepository.save(Cart.builder().customerId(customer.getId()).build());
        String token = jwtService.generateToken(customer, TokenType.ACCESS_TOKEN);
        saveToken(customer, token,TokenType.ACCESS_TOKEN);
        // send mail welcome to user
        String[] customerName = customer.getName().split(" ");
        emailService.sendWelcomeEmail(customer.getEmail(),customerName[customerName.length-1]);

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
        String token = jwtService.generateToken(userDetails,TokenType.ACCESS_TOKEN);
        revokeAllToken(userDetails);
        saveToken(userDetails, token, TokenType.ACCESS_TOKEN);
        AuthenticationResponse response = new AuthenticationResponse();
        response.setToken(token);
        return response;
    }

    public String forgotPassword(String email) {
        UserDetails user = userDetailsService.loadUserByUsername(email);
        String token = jwtService.generateToken(user,TokenType.FORGOT_PASSWORD_TOKEN);
        saveToken(user, token,TokenType.FORGOT_PASSWORD_TOKEN);
        return resetPasswordUrl + token;
    }

    public void resetPassword(String token, String newPassword) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtService.extractClaims(token, Claims::getSubject));
        if (!jwtService.validateToken(token, userDetails))
            throw new InvalidTokenException("Token is invalid or expired");
        if (userDetails.getAuthorities().isEmpty()) {
            Customer customer = (Customer) userDetails;
            customer.setPassword(passwordEncoder.encode(newPassword));
            customerRepository.save(customer);

        } else {
            Staff staff = ((Staff) userDetails);
            staff.setPassword(passwordEncoder.encode(newPassword));
            staffRepository.save(staff);
        }
        revokeAllToken(userDetails);
    }

    public void saveToken(UserDetails userDetails, String token,TokenType type) {
        Token t = Token.builder()
                .token(token)
                .type(type)
                .isRevoke(false)
                .build();
        t.setBelongToUser(belongToUser((userDetails)));
        tokenRepository.save(t);
    }

    public void revokeAllToken(UserDetails userDetails) {
        String belongToUser = belongToUser(userDetails);
        for (Token token : tokenRepository.findAllValidTokenByUser(belongToUser)) {
            token.setRevoke(true);
            tokenRepository.save(token);
        }
    }

    private String belongToUser(UserDetails userDetails) {
        if (userDetails instanceof Customer) {
            return "c" + ((Customer) userDetails).getId();
        }

        return "s" + ((Staff) userDetails).getId();
    }
}
