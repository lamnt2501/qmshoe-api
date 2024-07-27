package com.lamdangfixbug.qmshoe.product.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lamdangfixbug.qmshoe.discount.entity.Discount;
import com.lamdangfixbug.qmshoe.utils.Utils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String slug;
    private String description;

    @Column(nullable = false)
    private double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    private Brand brand;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"),
            name = "product_categories"
    )
    private List<Category> categories;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private List<ProductOption> productOptions;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private List<ProductImage> productImages;


    @ManyToOne
    @JoinColumn(name = "discount_id")
    @JsonManagedReference
    private Discount discount;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.slug = Utils.getSlug(this.name);
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
