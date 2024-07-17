package com.lamdangfixbug.qmshoe.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String city;
    private String district;
    private String specificAddress;
    private int customerId;
}
