package com.lamdangfixbug.qmshoe.user.payload.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateUserInformationRequest {
    private String phoneNumber;
    private String name;
    private String gender;
    private LocalDateTime birthday;
}
