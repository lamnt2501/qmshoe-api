package com.lamdangfixbug.qmshoe.product.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "discount")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double value;
    @Column(nullable = false)
    private double maxDiscount;
    @Column(nullable = false)
    private LocalDateTime startAt;
    @Column(nullable = false)
    private LocalDateTime endAt;

    @OneToMany(mappedBy = "discount")
    @JsonBackReference
    private List<Product> products;
}
