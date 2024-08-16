package com.lamdangfixbug.qmshoe.discount.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lamdangfixbug.qmshoe.product.entity.Product;
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
    private String type;
    @Column(nullable = false)
    private double maxUsage;
    @Column(nullable = false)
    private LocalDateTime startAt;
    @Column(nullable = false)
    private LocalDateTime endAt;
    private boolean isOrderDiscount;
    @OneToMany(mappedBy = "discount")
    @JsonBackReference
    private List<Product> products;
}
