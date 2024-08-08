package com.lamdangfixbug.qmshoe.auth.controller;

import com.lamdangfixbug.qmshoe.auth.payload.request.LoginRequest;
import com.lamdangfixbug.qmshoe.auth.payload.request.RegisterRequest;
import com.lamdangfixbug.qmshoe.auth.payload.response.AuthenticationResponse;
import com.lamdangfixbug.qmshoe.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @PostMapping("/forgot-password")
    public Map<String,String> forgotPassword(@RequestPart(name = "email")  String email) {
        Map<String ,String> res = new HashMap<>();
        res.put("resetPasswordUrl", authService.forgotPassword(email));
        return res;
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestParam String token,@RequestParam String newPassword) {
        authService.resetPassword(token,newPassword);
        Map<String, String> res = new HashMap<>();
        res.put("statusCode", "200");
        res.put("message", "Successfully Reset Password");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
