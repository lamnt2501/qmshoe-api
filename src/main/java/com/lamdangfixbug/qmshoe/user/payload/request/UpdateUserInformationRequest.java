package com.lamdangfixbug.qmshoe.user.payload.request;

import lombok.Data;

@Data
public class UpdateUserInformationRequest {
    private String phoneNumber;
    private String name;
}
