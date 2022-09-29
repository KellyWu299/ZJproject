package org.hnu.precomputation.service.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableScheduling
@SpringBootApplication
@MapperScan("org.hnu.precomputation.service.dao")
@ComponentScan("org.hnu.precomputation.*")
public class PrecomputationApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrecomputationApplication.class, args);
	}

}
