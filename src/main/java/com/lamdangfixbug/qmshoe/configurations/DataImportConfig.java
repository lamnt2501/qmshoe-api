package com.lamdangfixbug.qmshoe.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.lamdangfixbug.qmshoe.product.entity.*;
import com.lamdangfixbug.qmshoe.product.repository.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

@Configuration
public class DataImportConfig {
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final SizeRepository sizeRepository;
    private final ColorRepository colorRepository;
    private final ProductRepository productRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductImageRepository productImageRepository;

    public DataImportConfig(CategoryRepository categoryRepository, BrandRepository brandRepository,
                            SizeRepository sizeRepository, ColorRepository colorRepository,
                            ProductRepository productRepository, ProductOptionRepository productOptionRepository,
                            ProductImageRepository productImageRepository) {
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.sizeRepository = sizeRepository;
        this.colorRepository = colorRepository;
        this.productRepository = productRepository;
        this.productOptionRepository = productOptionRepository;
        this.productImageRepository = productImageRepository;
    }


//  @Bean
    CommandLineRunner commandLineRunner() {
        return args -> loadData();
    }

    private void loadData() {
        Faker faker = new Faker();
        try (InputStreamReader stream = new InputStreamReader(new FileInputStream(ResourceUtils.getFile("classpath:data.json")))) {
            StringBuilder sb = new StringBuilder();
            while (stream.ready()) {
                sb.append((char) stream.read());
            }
            String json = sb.toString();
            ObjectMapper objectMapper = new ObjectMapper();
            ProductJson[] productJsons = objectMapper.readValue(json, ProductJson[].class);

            for (ProductJson productJson : productJsons) {
                // category
                for (String c : productJson.getCategory()) {
                    if (categoryRepository.findByName(c) == null) {
                        categoryRepository.save(Category.builder().name(c).imgUrl("https://saigonsneaker.com/wp-content/uploads/2022/12/IMG_1031.jpg").description("Danh mục - " + c).build());
                    }
                }

                for (ProductJsonOption o : productJson.getOptions()) {
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
                Map<String, String> brandImgUrl = getBrandImgUrl();
                if (brandRepository.findByName(productJson.getBrand()).isEmpty()) {
                    brandRepository.save(Brand.builder().description("Nhãn Hiệu - " + productJson.getBrand()).imgUrl(brandImgUrl.get(productJson.getBrand()))
                            .name(productJson.getBrand()).build());

                }

                //product
                List<Category> categories = new ArrayList<>(Arrays.stream(productJson.getCategory()).map(categoryRepository::findByName).toList());
                Product p = productRepository.save(Product.builder()
                        .name(productJson.getName())
                        .description(productJson.getDescription())
                        .brand(brandRepository.findByName(productJson.getBrand()).orElse(null))
                        .categories(categories)
                        .build());

                //product options
                for (ProductJsonOption o : productJson.getOptions()) {
                    Color c = colorRepository.findByName(o.getColor()).orElse(null);
                    for (int s : o.getSize()) {
                        List<ProductOption> po = productOptionRepository.findByProductAndColor(p, c);
                        double price = faker.random().nextInt(345000, 2500000);
                        if (!po.isEmpty()) {
                            price = po.getFirst().getPrice();
                        }
                        productOptionRepository.save(ProductOption.builder()
                                .price(price)
                                .product(p)
                                .size(sizeRepository.findBySize(String.valueOf(s)).orElse(null))
                                .quantity(faker.random().nextInt(10, 200))
                                .color(c).build());
                    }
                    // product image
                    for (String i : o.getImages()) {
                        productImageRepository.save(ProductImage.builder()
                                .color(c)
                                .product(p)
                                .url(i).build());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static @NotNull Map<String, String> getBrandImgUrl() {
        Map<String, String> brandImgUrl = new HashMap<>();
        brandImgUrl.put("Adidas", "https://download.logo.wine/logo/Adidas/Adidas-Logo.wine.png");
        brandImgUrl.put("Nike", "https://download.logo.wine/logo/Nike%2C_Inc./Nike%2C_Inc.-Nike-Logo.wine.png");
        brandImgUrl.put("New Balance", "https://download.logo.wine/logo/New_Balance/New_Balance-Logo.wine.png");
        brandImgUrl.put("Converse", "https://download.logo.wine/logo/Converse_(shoe_company)/Converse_(shoe_company)-Converse2-Logo.wine.png");
        brandImgUrl.put("Vans", "https://saigonsneaker.com/wp-content/uploads/2020/05/Vans-Saigon-Sneaker.png.webp");
        brandImgUrl.put("MLB", "https://saigonsneaker.com/wp-content/uploads/2020/12/MLB-Saigon-Sneaker.png.webp");
        brandImgUrl.put("Asics", "https://download.logo.wine/logo/Asics/Asics-Logo.wine.png");
        brandImgUrl.put("Balenciaga", "https://download.logo.wine/logo/Balenciaga/Balenciaga-Logo.wine.png");
        brandImgUrl.put("Alexander McQueen", "https://saigonsneaker.com/wp-content/uploads/2020/05/McQueen-Saigon-Sneaker.png.webp");
        brandImgUrl.put("Biti's Hunter", "https://inkythuatso.com/uploads/thumbnails/800/2021/11/logo-biti-s-inkythuatso-01-04-09-15-48.jpg");
        return brandImgUrl;
    }

    @Data
    private static class ProductJson {
        private String name;
        private String description;
        private String[] category;
        private String brand;
        private int price;
        private List<ProductJsonOption> options;
    }

    @Data
    private static class ProductJsonOption {
        private String color;
        private int[] size;
        private int quantity;
        private List<String> images;
    }
}
