package com.lamdangfixbug.qmshoe.auth.controller;

import com.lamdangfixbug.qmshoe.auth.payload.request.LoginRequest;
import com.lamdangfixbug.qmshoe.auth.payload.request.RegisterRequest;
import com.lamdangfixbug.qmshoe.auth.payload.response.AuthenticationResponse;
import com.lamdangfixbug.qmshoe.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest,
                                                           HttpServletResponse response) {
        AuthenticationResponse authenticationResponse = authService.register(registerRequest);
//        ResponseCookie cookie = ResponseCookie.from("_authTK",authenticationResponse.getToken())
//                .httpOnly(true)
//                .secure(false)
//                .maxAge(60*60)
//                .path("/api/v1")
//                .sameSite("Lax")
//                .build();
//        response.addHeader(HttpHeaders.SET_COOKIE,cookie.toString());
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        AuthenticationResponse authenticationResponse = authService.login(loginRequest);
//        ResponseCookie cookie = ResponseCookie.from("_authTK",authenticationResponse.getToken())
//                .httpOnly(true)
//                .secure(false)
//                .maxAge(60*60)
//                .path("/api/v1")
//                .sameSite("Lax")
//                .build();
//        response.addHeader(HttpHeaders.SET_COOKIE,cookie.toString());
        return ResponseEntity.ok(authenticationResponse);
    }
}
