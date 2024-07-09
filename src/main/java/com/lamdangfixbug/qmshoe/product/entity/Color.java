package com.lamdangfixbug.qmshoe.product.entity;

import com.lamdangfixbug.qmshoe.utils.Utils;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "colors")
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String name;
    private String slug;
    @Column(nullable = false)
    private String hex;

    @PrePersist
    protected void onCreate() {
        this.slug = Utils.getSlug(this.name);
    }

}
