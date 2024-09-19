package com.lamdangfixbug.qmshoe.auth.payload.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private LocalDateTime birthday;
    private String gender;
}
