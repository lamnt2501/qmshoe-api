package com.lamdangfixbug.qmshoe;

import com.github.javafaker.Faker;
import com.lamdangfixbug.qmshoe.product.entity.Brand;
import com.lamdangfixbug.qmshoe.product.entity.Category;
import com.lamdangfixbug.qmshoe.product.entity.Color;
import com.lamdangfixbug.qmshoe.product.entity.Size;
import com.lamdangfixbug.qmshoe.product.repository.BrandRepository;
import com.lamdangfixbug.qmshoe.product.repository.CategoryRepository;
import com.lamdangfixbug.qmshoe.product.repository.ColorRepository;
import com.lamdangfixbug.qmshoe.product.repository.SizeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QmShoeApplication {

	public static void main(String[] args) {
		SpringApplication.run(QmShoeApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(
			CategoryRepository categoryRepository,
			BrandRepository brandRepository,
			SizeRepository sizeRepository, ColorRepository colorRepository) {
		return args -> {
			Faker faker = new Faker();
			for(int i = 1; i <= 10; i++) {
				categoryRepository.save(Category.builder().name("category " + i).description(faker.lorem().sentence()).imgUrl(faker.internet().url()).build());
				brandRepository.save(Brand.builder().name("brand " + i).description(faker.lorem().sentence()).build());
				colorRepository.save(Color.builder().name("color " + i).hex(faker.color().hex()).build());
			}
			for (int i = 20; i<= 45 ;i++){
				sizeRepository.save(Size.builder().size(i+"").description(faker.lorem().sentence()).build());
			}
		};
	}
}
