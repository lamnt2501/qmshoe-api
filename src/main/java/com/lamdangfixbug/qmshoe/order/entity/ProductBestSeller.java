package com.lamdangfixbug.qmshoe.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_best_seller")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductBestSeller {
    @Id
    private String sku;
    private int sold;
}
