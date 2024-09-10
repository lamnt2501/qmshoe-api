package com.lamdangfixbug.qmshoe.user.payload.response;

import com.lamdangfixbug.qmshoe.user.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String avtUrl;
    private String gender;
    private LocalDateTime birthday;
    public static CustomerResponse from(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhoneNumber())
                .avtUrl(customer.getAvtUrl())
                .gender(customer.getGender().name())
                .birthday(customer.getBirthday())
                .build();
    }

}
