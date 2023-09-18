package com.poseidon.inventory.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.poseidon.inventory")
@EntityScan(basePackages = "com.poseidon.inventory.model")
@EnableJpaRepositories(basePackages = "com.poseidon.inventory.repository")
public class PoseidonApplication {

	public static void main(String[] args) {
		SpringApplication.run(PoseidonApplication.class, args);
	}

}
