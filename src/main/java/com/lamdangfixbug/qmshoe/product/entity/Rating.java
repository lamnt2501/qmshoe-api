package com.lamdangfixbug.qmshoe.product.entity;

import com.lamdangfixbug.qmshoe.user.entity.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private double ratingValue;
    @Column(nullable = false)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId",referencedColumnName = "id")
    private Customer customer;
    private int productId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
//    private boolean isApproved;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
