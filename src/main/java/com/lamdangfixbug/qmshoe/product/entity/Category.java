package com.lamdangfixbug.qmshoe.product.entity;


import com.lamdangfixbug.qmshoe.utils.Utils;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false,unique = true)
    private String name;
    private String slug;
    private String description;
    private String imgUrl;

    @PrePersist
    protected void onCreate() {
        this.slug = Utils.getSlug(this.name);
    }
}
