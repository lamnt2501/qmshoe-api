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
                    "where p.is_active = 1 " +
                    "and c.category_id = :category " +
                    "and pd.price between :minPrice and :maxPrice " +
                    "and pd.color_id in :colors " +
                    "and pd.size_id in :sizes " +
                    "and p.name like %:name%"
    )
    List<Product> getFilteredProduct(@Param("category") int categoryId,
                                     @Param("minPrice") double minPrice,
                                     @Param("maxPrice") double maxPrice,
                                     @Param("colors") Collection<Integer> colors,
                                     @Param("sizes") Collection<Integer> sizes,
                                     @Param("name") String name,
//                                     @Param("active") Collection<Boolean> active,
                                     Pageable pageable);

    @Query(value = "select  p.* from products as p where p.is_active in :active", nativeQuery = true)
    List<Product> getAllProduct(Collection<Integer> active, Pageable pageable);

    @Query("select p from Product p ,ProductOption  po where " +
            "p.id = po.product.id" +
            " and po.sku = :sku")
    Product findBySku(String sku);
}
