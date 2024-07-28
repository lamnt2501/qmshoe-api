package com.lamdangfixbug.qmshoe.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.lamdangfixbug.qmshoe.product.entity.*;
import com.lamdangfixbug.qmshoe.product.repository.*;
import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataImportConfig {
    @Data
    public static class DTO {
        private String name;
        private String description;
        private String[] category;
        private String brand;
        private int price;
        private List<Options> options;
    }

    @Data
    public static class Options {
        private String color;
        private int[] size;
        private int quantity;
        private List<String> images;
    }

//    @Bean
    CommandLineRunner commandLineRunner(
            CategoryRepository categoryRepository,
            BrandRepository brandRepository,
            SizeRepository sizeRepository,
            ColorRepository colorRepository,
            ProductRepository productRepository,
            ProductOptionRepository productOptionRepository,
            ProductImageRepository productImageRepository) {
        return args -> {
            Faker faker = new Faker();
            File file = ResourceUtils.getFile("classpath:data.json");
            try (InputStreamReader stream = new InputStreamReader(new FileInputStream(file))) {
                StringBuilder sb = new StringBuilder();
                while (stream.ready()) {
                    sb.append((char) stream.read());
                }
                String json = sb.toString();
                ObjectMapper objectMapper = new ObjectMapper();
                DataImportConfig.DTO[] dtos = objectMapper.readValue(json, DataImportConfig.DTO[].class);

                for (DataImportConfig.DTO dto : dtos) {
                    // category
                    for (String c : dto.getCategory()) {
                        if (categoryRepository.findByName(c) == null) {
                            categoryRepository.save(Category.builder().name(c).description("Danh mục - " + c).build());
                        }
                    }

                    for (DataImportConfig.Options o : dto.getOptions()) {
                        // color
                        if (colorRepository.findByName(o.getColor()).isEmpty()) {
                            colorRepository.save(Color.builder()
                                    .name(o.getColor()).hex(faker.color().hex(true)).build());
                        }
                        //size
                        for (int s : o.getSize()) {
                            if (sizeRepository.findBySize(String.valueOf(s)).isEmpty()) {
                                sizeRepository.save(Size.builder()
                                        .size(String.valueOf(s))
                                        .description("Size: " + s).build());
                            }
                        }
                    }
                    // brand
                    Brand brand = null;
                    if (brandRepository.findByName(dto.getBrand()).isEmpty()) {
                        brand = brandRepository.save(Brand.builder().description("Nhãn Hiệu - " + dto.getBrand())
                                .name(dto.getBrand()).build());
                    }

                    List<Category> categories = new ArrayList<>(Arrays.stream(dto.getCategory()).map(categoryRepository::findByName).toList());
                    Product p = productRepository.save(Product.builder()
                            .name(dto.getName())
                            .description(dto.getDescription())
                            .brand(brand)
                            .categories(categories)
                            .build());

                    for (Options o : dto.getOptions()) {
                        Color c = colorRepository.findByName(o.getColor()).get();
                        for (int s : o.getSize()) {
                            productOptionRepository.save(ProductOption.builder()
                                    .price(faker.random().nextInt(345000, 2500000))
                                    .product(p)
                                    .size(sizeRepository.findBySize(String.valueOf(s)).get())
                                    .quantity(faker.random().nextInt(10, 200))
                                    .color(c).build());
                        }
                        for (String i : o.getImages()) {
                            productImageRepository.save(ProductImage.builder()
                                    .color(c)
                                    .product(p)
                                    .url(i).build());
                        }
                    }
                }


            } catch (Exception e) {

            }

        };
    }
}
