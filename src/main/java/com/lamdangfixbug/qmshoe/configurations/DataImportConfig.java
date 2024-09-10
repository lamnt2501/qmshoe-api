package com.lamdangfixbug.qmshoe.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.lamdangfixbug.qmshoe.cart.entity.Cart;
import com.lamdangfixbug.qmshoe.cart.repository.CartRepository;
import com.lamdangfixbug.qmshoe.order.entity.Order;
import com.lamdangfixbug.qmshoe.order.entity.TopCustomer;
import com.lamdangfixbug.qmshoe.order.repository.OrderRepository;
import com.lamdangfixbug.qmshoe.order.repository.TopCustomerRepository;
import com.lamdangfixbug.qmshoe.product.entity.*;
import com.lamdangfixbug.qmshoe.product.repository.*;
import com.lamdangfixbug.qmshoe.user.entity.*;
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
import java.time.LocalDateTime;
import java.time.Month;
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
    private final CartRepository cartRepository;

    public DataImportConfig(CategoryRepository categoryRepository, BrandRepository brandRepository,
                            SizeRepository sizeRepository, ColorRepository colorRepository,
                            ProductRepository productRepository, ProductOptionRepository productOptionRepository,
                            ProductImageRepository productImageRepository, CustomerRepository customerRepository, CustomerRepository customerRepository1, TopCustomerRepository topCustomerRepository, StaffRepository staffRepository, RatingRepository ratingRepository, OrderRepository orderRepository, CartRepository cartRepository) {
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
        this.cartRepository = cartRepository;
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
            String[] avtUrl = {
                    "https://scontent.fhan5-2.fna.fbcdn.net/v/t1.6435-9/79688722_568320937296602_7578883004005613568_n.jpg?stp=dst-jpg_s600x600&_nc_cat=102&ccb=1-7&_nc_sid=f727a1&_nc_ohc=1vOlnmdOK5UQ7kNvgHWZYst&_nc_ht=scontent.fhan5-2.fna&oh=00_AYAi-QJbxcktfvAK4TpQRxS9Jf53Hipipu1gu__RjwJ-dw&oe=6707943C",
                    "https://www.facebook.com/photo/?fbid=568320980629931&set=pcb.568327270629302&__cft__[0]=AZW2PE6wdBE3wUwDrSFR2PvMesf8mUVAgAR1Lz4eIoLRv8i3tElVugOp6-ybkAWAoQmucoYqVPundHYZvdf0fEb_fDkn4kZkrP8BnsJ3PuukjjCbrZY22fZhB6UNy6SxwgLtOV6in5-oMqdPSRwwjpPSMFyJ93H0bkUikoGm91Af6SRvGEZpD1S1ssmHJwntDLI&__tn__=*bH-R",
                    "https://scontent.fhan5-6.fna.fbcdn.net/v/t1.6435-9/79792611_568321050629924_3933262854186074112_n.jpg?stp=dst-jpg_s600x600&_nc_cat=105&ccb=1-7&_nc_sid=f727a1&_nc_ohc=a0O_uUOxHMcQ7kNvgEZCSI1&_nc_ht=scontent.fhan5-6.fna&oh=00_AYB4bKonlUGCwyPS1F3kHZp0oWYIFYvGzmgPsmUdQaq9Sw&oe=670793CB",
                    "https://scontent.fhan5-9.fna.fbcdn.net/v/t1.6435-9/79708883_568321130629916_4581882986957373440_n.jpg?stp=dst-jpg_s600x600&_nc_cat=109&ccb=1-7&_nc_sid=f727a1&_nc_ohc=gQlST5BZ61kQ7kNvgGl5DnV&_nc_ht=scontent.fhan5-9.fna&oh=00_AYApG6WIGZ0Y2jGE1W8eRMGx8AlGtfPJ1cfG6-wx08siew&oe=67078A15",
                    "https://scontent.fhan5-2.fna.fbcdn.net/v/t1.6435-9/79166428_568321180629911_4327462013539188736_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=f727a1&_nc_ohc=i1GYtr-HSqUQ7kNvgEztplE&_nc_ht=scontent.fhan5-2.fna&oh=00_AYCtRpWzUlEj97opDv-lTmPoWuRHp4QSl5HtLiDu2E7mmg&oe=670777B2",
                    "https://scontent.fhan5-8.fna.fbcdn.net/v/t1.6435-9/80079726_568321230629906_8565785811960725504_n.jpg?_nc_cat=108&ccb=1-7&_nc_sid=f727a1&_nc_ohc=HNlB8OiqDfIQ7kNvgGJFxS2&_nc_ht=scontent.fhan5-8.fna&oh=00_AYBavQwZbgRTHPPbs5OEIxhglieZzHdZ_GCK_O5s9vxUhA&oe=67077357",
                    "https://scontent.fhan5-2.fna.fbcdn.net/v/t1.6435-9/79170403_568321300629899_819284966130057216_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=f727a1&_nc_ohc=tLh4HszUj00Q7kNvgH0-r6W&_nc_ht=scontent.fhan5-2.fna&oh=00_AYAbRkiCO31LahH2uiO7BEY6WthYxI47y15IofLmYoGLNA&oe=67079673",
                    "https://scontent.fhan5-6.fna.fbcdn.net/v/t1.6435-9/79722551_568321350629894_8480869609824583680_n.jpg?_nc_cat=107&ccb=1-7&_nc_sid=f727a1&_nc_ohc=YYJdvWog0U8Q7kNvgFQmpHD&_nc_ht=scontent.fhan5-6.fna&_nc_gid=A8EoQ5VS7VlDnTvp_TGuiph&oh=00_AYB_5MWIhb0zvuAdfm5cOt2JZ4DXIv0RPJfMpELGRLy6RA&oe=6707933A",
                    "https://scontent.fhan5-9.fna.fbcdn.net/v/t1.6435-9/78614590_568324453962917_2556700242107957248_n.jpg?_nc_cat=110&ccb=1-7&_nc_sid=f727a1&_nc_ohc=Eo2l5N8vhD4Q7kNvgHTIjh4&_nc_ht=scontent.fhan5-9.fna&oh=00_AYA2-RP4gsXOzMDqHgYyQ0tg3-tpNm1x-FPfDXwZLrXclA&oe=670784C6",
                    "https://scontent.fhan5-6.fna.fbcdn.net/v/t1.6435-9/78641502_568324497296246_7140851306805067776_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=f727a1&_nc_ohc=ei3GYJ3v8iIQ7kNvgGwvrMc&_nc_ht=scontent.fhan5-6.fna&_nc_gid=Afwl4vO6FrOmOd3w3jtHr16&oh=00_AYALiFBJ3a0yOW1yxcWTtxrOVaO8E3_ECFTeJbxRPL5V_A&oe=67077AE3",
                    "https://scontent.fhan5-2.fna.fbcdn.net/v/t1.6435-9/78860225_568324553962907_3203405296206610432_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=f727a1&_nc_ohc=gNAHnxvHg_IQ7kNvgEcqmM1&_nc_ht=scontent.fhan5-2.fna&oh=00_AYDInL0DjViOLsAE-f_5niihiigk5L3C8AN9tZAoX5V9bg&oe=6707A491",
                    "https://scontent.fhan5-9.fna.fbcdn.net/v/t1.6435-9/75402298_568324603962902_7517923695654862848_n.jpg?_nc_cat=110&ccb=1-7&_nc_sid=f727a1&_nc_ohc=WilEIVZ_WrcQ7kNvgGPiYCn&_nc_ht=scontent.fhan5-9.fna&_nc_gid=AYB47K8rFlIkaaZhePTAghs&oh=00_AYD98wWSrdlX2vSTYFQBGdXwQP-g3KocQmlFaK7YSSmTaA&oe=67078236",
                    "https://scontent.fhan5-8.fna.fbcdn.net/v/t1.6435-9/79181945_568324640629565_4773055382916431872_n.jpg?_nc_cat=108&ccb=1-7&_nc_sid=f727a1&_nc_ohc=KkJ_-wHMJbYQ7kNvgFOMfQQ&_nc_ht=scontent.fhan5-8.fna&_nc_gid=A-i4sEWWNmuoo1yiLXDMIgr&oh=00_AYCzKGsBgqwVCXGnf0Zq4CjAZa47116EC8LKTnxhOxlNpQ&oe=67078FC7",
                    "https://scontent.fhan5-11.fna.fbcdn.net/v/t1.6435-9/79622849_568324943962868_1430929910133686272_n.jpg?_nc_cat=103&ccb=1-7&_nc_sid=f727a1&_nc_ohc=KqOMrJe5GZYQ7kNvgGsMS58&_nc_ht=scontent.fhan5-11.fna&_nc_gid=ABgcHFQXe8_HMlik618GAuX&oh=00_AYALeyz2xN7ThRGLARtzlSO2BUMGY99sk1Sk96N57oK9Ng&oe=670794B6"
            };
            for (int i = 1; i <= 100; i++) {
                Customer c = customerRepository.save(Customer.builder()
                        .name(fakerVn.name().fullName())
                        .email(fakerEn.internet().emailAddress())
                        .birthday(LocalDateTime.of(random.nextInt(1980, 2011), Month.of(random.nextInt(1, 13)), random.nextInt(1, 31), 0, 0))
                        .gender(Gender.valueOf(i % 2 == 0 ? "MALE" : "FEMALE"))
                        .avtUrl(avtUrl[random.nextInt(avtUrl.length)])
                        .phoneNumber("0" + random.nextInt(200000000, 999999999)).password(password).build());
                customers.add(c);
                topCustomerRepository.save(TopCustomer.builder().customer(c).spend(0).memberShipClass("").build());
                cartRepository.save(Cart.builder().customerId(c.getId()).build());
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
                    "Tôi không thấy màu giống trên ảnh lắm",
                    "Mua tặng vợ đẹp hết xảy, vợ khen quá trời",
                    "Tôi sẽ cho một sao",
                    "Giao hàng nhanh, nhiều ưu đãi, ủng hộ shop lâu dài",
                    "Giày bị chật nhưng không kịp đổi hàng :(",
                    "Chua di da biet dep roi, toi chi rate lay qua thui hihi",
                    "Kha la ung shop nay, phuc vu tan tinh ma lai de thuong nua, admin dep trai, giay cung dep nua"
            };
            for (Product p : products) {
                for (Customer c : customers) {
                    int value = random.nextInt(2, 6);
                    int totalRating = (int) ratingRepository.countByProductId(p.getId());
                    p.setAvgRatings((p.getAvgRatings() * totalRating + value) / (totalRating + 1));
                    ratingRepository.save(Rating.builder().ratingValue(value)
                            .productId(p.getId()).customer(c).comment(fakeRatings[random.nextInt(fakeRatings.length)])
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
