package com.lamdangfixbug.qmshoe;

import com.github.javafaker.Faker;
import com.lamdangfixbug.qmshoe.product.entity.*;
import com.lamdangfixbug.qmshoe.product.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Random;

@SpringBootApplication
public class QmShoeApplication {

    public static void main(String[] args) {
        SpringApplication.run(QmShoeApplication.class, args);
    }

//    @Bean
    CommandLineRunner commandLineRunner(
            CategoryRepository categoryRepository,
            BrandRepository brandRepository,
            SizeRepository sizeRepository,
            ColorRepository colorRepository, ProductRepository productRepository, ProductOptionRepository productOptionRepository, ProductImageRepository productImageRepository) {
        return args -> {
            Faker faker = new Faker();
            for (int i = 1; i <= 10; i++) {
                categoryRepository.save(Category.builder().name("category " + i).description(faker.lorem().sentence())
                        .imgUrl("https://cdn.pixabay.com/photo/2021/10/11/23/49/app-6702045_1280.png").build());
                brandRepository.save(Brand.builder().name("brand " + i).description(faker.lorem().sentence()).build());
                colorRepository.save(Color.builder().name("color " + i).hex(faker.color().hex()).build());
            }
            for (int i = 20; i <= 45; i++) {
                sizeRepository.save(Size.builder().size(i + "").description(faker.lorem().sentence()).build());
            }
            List<Brand> brands = brandRepository.findAll();
            List<Category> categories = categoryRepository.findAll();
            List<Color> colors = colorRepository.findAll();
            List<Size> sizes = sizeRepository.findAll();
            Random random = new Random();
            for (int i = 1; i <= 100; i++) {
                int finalI = i;
                productRepository.save(Product.builder()
                        .name(faker.commerce().productName())
                        .price(Math.random() * 100000 + 100000)
                        .description(faker.lorem().sentence())
                        .brand(brands.get(random.nextInt(10)))
                        .categories(
                                categories.stream()
                                        .filter(category -> finalI % category.getId() == 0)
                                        .toList())
                        .build());
            }
            List<Product> products = productRepository.findAll();
            for (Product product : products) {
                for (int i = 1; i <= 3; i++) {
                    ProductOption po = ProductOption.builder()
                            .color(colors.get(random.nextInt(10)))
                            .size(sizes.get(random.nextInt(10)))
                            .product(product)
                            .quantity(random.nextInt(191) + 10)
                            .build();
                    try {
                        productOptionRepository.save(po);
                        for (int j = 1; j <= 3; j++) {
                            productImageRepository.save(
                                    ProductImage.builder()
                                            .color(po.getColor())
                                            .product(product)
                                            .url("https://cdn.pixabay.com/photo/2018/10/13/05/24/shoes-men-3743513_640.jpg")
                                            .build()
                            );
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }

                }
            }

        };
    }
}
