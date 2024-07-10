package com.lamdangfixbug.qmshoe.product.repository;

import com.lamdangfixbug.qmshoe.product.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findBySlug(String slug);

    List<Product> findAllByCategories_SlugAndProductOptions_Color_SlugInAndProductOptions_Size_SizeIn(String categories_slug, Collection<String> productOptions_color_slug, Collection<String> productOptions_size_size, Pageable pageable);

    @Query(nativeQuery = true,
            value = "select distinct p.* from products as p " +
                    "join product_categories as c on c.product_id = p.id " +
                    "join product_details as pd on pd.product_id = p.id " +
                    "where c.category_id = :category " +
                    "and p.price between :minPrice and :maxPrice " +
                    "and pd.color_id in :colors " +
                    "and pd.size_id in :sizes")
    List<Product> getFilteredProduct(@Param("category") int categoryId,
                                     @Param("minPrice") double minPrice,
                                     @Param("maxPrice") double maxPrice,
                                     @Param("colors") Collection<Integer> colors,
                                     @Param("sizes") Collection<Integer> sizes,
                                     Pageable pageable);
}
