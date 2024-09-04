package com.lamdangfixbug.qmshoe.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.lamdangfixbug.qmshoe.order.entity.Order;
import com.lamdangfixbug.qmshoe.order.entity.TopCustomer;
import com.lamdangfixbug.qmshoe.order.repository.OrderRepository;
import com.lamdangfixbug.qmshoe.order.repository.TopCustomerRepository;
import com.lamdangfixbug.qmshoe.product.entity.*;
import com.lamdangfixbug.qmshoe.product.repository.*;
import com.lamdangfixbug.qmshoe.user.entity.Address;
import com.lamdangfixbug.qmshoe.user.entity.Customer;
import com.lamdangfixbug.qmshoe.user.entity.Role;
import com.lamdangfixbug.qmshoe.user.entity.Staff;
import com.lamdangfixbug.qmshoe.user.repository.CustomerRepository;
import com.lamdangfixbug.qmshoe.user.repository.StaffRepository;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.Instant;
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
    private final CustomerRepository customerRepository;
    private final TopCustomerRepository topCustomerRepository;
    private final StaffRepository staffRepository;
    private final RatingRepository ratingRepository;
    private final OrderRepository orderRepository;

    public DataImportConfig(CategoryRepository categoryRepository, BrandRepository brandRepository,
                            SizeRepository sizeRepository, ColorRepository colorRepository,
                            ProductRepository productRepository, ProductOptionRepository productOptionRepository,
                            ProductImageRepository productImageRepository, CustomerRepository customerRepository, CustomerRepository customerRepository1, TopCustomerRepository topCustomerRepository, StaffRepository staffRepository, RatingRepository ratingRepository, OrderRepository orderRepository) {
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.sizeRepository = sizeRepository;
        this.colorRepository = colorRepository;
        this.productRepository = productRepository;
        this.productOptionRepository = productOptionRepository;
        this.productImageRepository = productImageRepository;
        this.customerRepository = customerRepository1;
        this.topCustomerRepository = topCustomerRepository;
        this.staffRepository = staffRepository;
        this.ratingRepository = ratingRepository;
        this.orderRepository = orderRepository;
    }


    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> loadData();
    }

    private void loadData() {
        Random random = new Random();
        String password = new BCryptPasswordEncoder().encode("12345678");
        Faker fakerVn = new Faker(Locale.forLanguageTag("vi"));
        Faker fakerEn = new Faker();

        staffRepository.save(Staff.builder().email("admin@gmail.com").password(password).name("Liam").role(Role.ADMIN).build());
        try (InputStreamReader stream = new InputStreamReader(new FileInputStream(ResourceUtils.getFile("classpath:data.json")))) {
            StringBuilder sb = new StringBuilder();

            // user
            List<Customer> customers = new ArrayList<>();
            for (int i = 1; i <= 100; i++) {
                Customer c = customerRepository.save(Customer.builder()
                        .name(fakerVn.name().fullName())
                        .email(fakerEn.internet().emailAddress())
                        .phoneNumber("0" + random.nextInt(200000000, 999999999)).password(password).build());
                customers.add(c);
                topCustomerRepository.save(TopCustomer.builder().customer(c).spend(0).memberShipClass("").build());
            }

            while (stream.ready()) {
                sb.append((char) stream.read());
            }

            String json = sb.toString();
            ObjectMapper objectMapper = new ObjectMapper();
            ProductJson[] productJsons = objectMapper.readValue(json, ProductJson[].class);

            List<Product> products = new ArrayList<>();
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
                                .name(o.getColor()).hex(fakerEn.color().hex(true)).build());
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
                        .isActive(true)
                        .build());
                products.add(p);
                //product options
                for (ProductJsonOption o : productJson.getOptions()) {
                    Color c = colorRepository.findByName(o.getColor()).orElse(null);
                    for (int s : o.getSize()) {
                        List<ProductOption> po = productOptionRepository.findByProductAndColor(p, c);
                        double price = fakerEn.random().nextInt(345000, 2500000);
                        if (!po.isEmpty()) {
                            price = po.getFirst().getPrice();
                        }
                        productOptionRepository.save(ProductOption.builder()
                                .price(price)
                                .product(p)
                                .size(sizeRepository.findBySize(String.valueOf(s)).orElse(null))
                                .quantity(fakerEn.random().nextInt(10, 200))
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
            String[] fakeRatings = {
                    "Hàng đẹp, đúng mẫu mình thích đúng màu",
                    "Cũng đẹp, đáng tiền. Lần sau sẽ quay lại",
                    "Tôi không thấy màu going trên ảnh lắm",
                    "Mua tặng vợ đẹp hết xảy, vợ khen quá trời",
                    "Tôi sẽ cho một sao",
                    "Giao hàng nhanh, nhiều ưu đãi, ủng hộ shop lâu dài",
                    "Giày bị chật nhưng không kịp đổi hàng :("
            };
            for(Product p : products){
                for(Customer c : customers){
                    int value = random.nextInt(3,6);
                    int totalRating = (int)ratingRepository.countByProductId(p.getId());
                    p.setAvgRatings((p.getAvgRatings()*totalRating+value)/(totalRating+1));
                    ratingRepository.save(Rating.builder().ratingValue(value)
                            .productId(p.getId()).customer(c).comment(fakeRatings[random.nextInt(7)])
                            .build());
                    productRepository.save(p);
                }
            }
//            for(int i = 1; i<=200; i++){
//                Order o = orderRepository.save(Order.builder().build());
//            }

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
